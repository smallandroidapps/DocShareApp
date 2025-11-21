# DocShareApp
Request and approve the documents

## Google Sign-In & Firebase Setup

This app integrates Google Sign-In with Firebase Auth and creates a user profile in Firestore at `users/{uid}`.

### Configure Firebase
- In Firebase Console, create a project and add an Android app with package `com.docshare.docshare`.
- Generate SHA-1 and SHA-256 for your debug/release keystores:
  - `./gradlew signingReport`
  - Or use Android Studio Gradle panel (Gradle > Tasks > android > signingReport).
- Add both fingerprints in Firebase Console → Project Settings → App → Add Fingerprint.
- Download `google-services.json` and place it in `DocShareApp/app/`.

### Missing Configuration Guards
- If `google-services.json` is missing or incomplete, the login screen shows:
  - “App not configured for Google Sign-In. Please add google-services.json to app/”.
- Build system applies the Google Services plugin only when `app/google-services.json` exists.

### Dependencies
- Firebase BOM `33.5.1`
- `firebase-auth`, `firebase-firestore`
- Google Play Services Auth `21.2.0`

### After Sign-In
- The app creates/ensures a Firestore document `users/{uid}` with fields:
  - `name`, `email`, `photoUrl`, `createdAt` (server timestamp)
