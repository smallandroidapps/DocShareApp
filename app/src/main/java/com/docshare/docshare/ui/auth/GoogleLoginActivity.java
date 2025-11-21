package com.docshare.docshare.ui.auth;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.docshare.docshare.MainActivity;
import com.docshare.docshare.databinding.ActivityGoogleLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GoogleLoginActivity extends AppCompatActivity {

    private ActivityGoogleLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(Exception.class);
                        String idToken = account.getIdToken();
                        if (idToken == null) {
                            showError("No ID token from Google. Invalid configuration.");
                            setLoading(false);
                            return;
                        }
                        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                ensureFirestoreProfile(user);
                                navigateHome();
                            } else {
                                showError("Firebase sign-in failed: " + authTask.getException());
                            }
                            setLoading(false);
                        });
                    } catch (Exception e) {
                        showError("Google sign-in failed: " + e.getMessage());
                        setLoading(false);
                    }
                } else {
                    showError("Sign-in cancelled");
                    setLoading(false);
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Guard Firebase initialization when configuration is missing
        try {
            FirebaseApp.initializeApp(this);
        } catch (IllegalStateException ignored) { /* already initialized or missing config */ }

        firebaseAuth = FirebaseAuth.getInstance();

        // Determine default_web_client_id presence
        int webClientIdResId = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
        if (webClientIdResId == 0) {
            binding.btnGoogleSignIn.setEnabled(false);
            showError("App not configured for Google Sign-In. Please add google-services.json to app/.");
        } else {
            String clientId = getString(webClientIdResId);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(clientId)
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        binding.btnGoogleSignIn.setOnClickListener(v -> {
            if (googleSignInClient == null) {
                showError("Google Sign-In not available. Missing configuration.");
                return;
            }
            setLoading(true);
            signInLauncher.launch(googleSignInClient.getSignInIntent());
        });
    }

    private void setLoading(boolean loading) {
        binding.progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnGoogleSignIn.setEnabled(!loading);
    }

    private void showError(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void navigateHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void ensureFirestoreProfile(@Nullable FirebaseUser user) {
        if (user == null) return;
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String uid = user.getUid();
            db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
                if (doc.exists()) return;
                String name = user.getDisplayName();
                String email = user.getEmail();
                Uri photoUri = user.getPhotoUrl();
                Map<String, Object> profile = new HashMap<>();
                if (name != null) profile.put("name", name);
                if (email != null) profile.put("email", email);
                profile.put("photoUrl", photoUri != null ? photoUri.toString() : null);
                profile.put("createdAt", FieldValue.serverTimestamp());
                db.collection("users").document(uid).set(profile)
                        .addOnFailureListener(e -> showError("Failed to create profile: " + e.getMessage()));
            }).addOnFailureListener(e -> showError("Failed to fetch profile: " + e.getMessage()));
        } catch (Exception e) {
            showError("Firestore not configured: " + e.getMessage());
        }
    }
}

