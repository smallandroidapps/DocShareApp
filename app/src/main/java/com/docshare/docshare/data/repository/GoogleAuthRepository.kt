package com.docshare.docshare.data.repository

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.docshare.docshare.R

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val user: FirebaseUser) : AuthState
    data class Error(val msg: String) : AuthState
}

class GoogleAuthRepository(
    private val context: Context,
    private val auth: FirebaseAuth
) {
    private val googleSignInClient: GoogleSignInClient
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    suspend fun handleGoogleResult(data: Intent?) {
        if (data == null) {
            _authState.value = AuthState.Error("Sign-in cancelled")
            return
        }
        _authState.value = AuthState.Loading
        try {
            val account = withContext(Dispatchers.IO) {
                // Synchronously wait for result to simplify coroutine interop
                Tasks.await(GoogleSignIn.getSignedInAccountFromIntent(data))
            }
            val idToken = account.idToken
            if (idToken.isNullOrEmpty()) {
                _authState.value = AuthState.Error("No ID token from Google. Check configuration.")
                return
            }
            firebaseSignIn(idToken)
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Google sign-in failed: ${e.message}")
        }
    }

    private suspend fun firebaseSignIn(idToken: String) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = withContext(Dispatchers.IO) {
                Tasks.await(auth.signInWithCredential(credential))
            }
            val user = result.user
            if (user != null) {
                _authState.value = AuthState.Success(user)
            } else {
                _authState.value = AuthState.Error("Firebase user null after sign-in")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Firebase sign-in failed: ${e.message}")
        }
    }

    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
        _authState.value = AuthState.Idle
    }
}

