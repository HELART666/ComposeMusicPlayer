package com.example.composemusicplayer.data.repository

import com.example.composemusicplayer.data.api.AuthApi
import com.example.composemusicplayer.domain.models.AuthRequest
import com.example.composemusicplayer.domain.models.AuthResponse
import com.example.composemusicplayer.domain.repository.AuthRepository
import com.example.composemusicplayer.state.ScreenState
import com.example.composemusicplayer.utils.BaseRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : BaseRepository(), AuthRepository {

    override suspend fun auth(authRequest: AuthRequest): ScreenState<AuthResponse> =
        safeApiCall {
            authApi.login(authRequest)
        }

    override suspend fun refresh(refreshToken: String): ScreenState<AuthResponse> =
        safeApiCall {
            authApi.refresh(refreshToken)
        }
}