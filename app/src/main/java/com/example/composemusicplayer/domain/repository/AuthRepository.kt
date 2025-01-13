package com.example.composemusicplayer.domain.repository

import com.example.composemusicplayer.domain.models.AuthRequest
import com.example.composemusicplayer.domain.models.AuthResponse
import com.example.composemusicplayer.state.ScreenState

interface AuthRepository {

    suspend fun auth(authRequest: AuthRequest): ScreenState<AuthResponse>

    suspend fun refresh(refreshToken: String): ScreenState<AuthResponse>

}