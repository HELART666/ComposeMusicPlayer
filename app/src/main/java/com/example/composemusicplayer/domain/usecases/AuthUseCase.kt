package com.example.composemusicplayer.domain.usecases

import com.example.composemusicplayer.domain.models.AuthRequest
import com.example.composemusicplayer.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun auth(authRequest: AuthRequest) = authRepository.auth(authRequest)
}