package com.docshare.docshare.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docshare.docshare.data.repository.GoogleAuthRepository
import com.docshare.docshare.data.repository.AuthState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: GoogleAuthRepository
) : ViewModel() {
    val authState = repo.authState

    fun getGoogleIntent() = repo.getSignInIntent()

    fun handleSignInResult(data: Intent?) {
        viewModelScope.launch { repo.handleGoogleResult(data) }
    }

    fun signOut() = repo.signOut()
}

