package com.example.composemusicplayer.data.api

import com.example.composemusicplayer.domain.models.AuthRequest
import com.example.composemusicplayer.domain.models.AuthResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/refresh")
    fun refresh(@Body refreshToken: String): Response<AuthResponse>

    companion object {
        fun create(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
}