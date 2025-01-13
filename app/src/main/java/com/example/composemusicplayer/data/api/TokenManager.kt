package com.example.composemusicplayer.data.api

import android.content.Context

class TokenManager(private val context: Context) {
    companion object {
        const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val USER_ID_KEY = "user_id"
    }

    fun saveUserId(id: Long) {
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).edit().apply {
            putLong(USER_ID_KEY, id)
            apply()
        }
    }

    fun getUserId(): Long {
        return context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).getLong(USER_ID_KEY, -1L)
    }

    fun saveAccessToken(token: String) {
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).edit().apply {
            putString(ACCESS_TOKEN_KEY, token)
            apply()
        }
    }

    fun getAccessToken(): String? {
        return context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).getString(ACCESS_TOKEN_KEY, null)
    }

    fun saveRefreshToken(token: String) {
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).edit().apply {
            putString(REFRESH_TOKEN_KEY, token)
            apply()
        }
    }

    fun getRefreshToken(): String? {
        return context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).getString(REFRESH_TOKEN_KEY, null)
    }
}
