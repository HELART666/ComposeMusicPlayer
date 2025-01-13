package com.example.composemusicplayer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemusicplayer.data.api.TokenManager
import com.example.composemusicplayer.domain.models.AuthRequest
import com.example.composemusicplayer.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenManager: TokenManager
): ViewModel() {

    fun auth(authRequest: AuthRequest, onSuccessAuth: () -> Unit) {
        viewModelScope.launch {
            authUseCase.auth(authRequest).let {
                it.data?.let { tokens ->
                    tokenManager.saveAccessToken(tokens.accessToken)
                    tokenManager.saveAccessToken(tokens.refreshToken)
                    onSuccessAuth()
                }
            }
        }
    }

}