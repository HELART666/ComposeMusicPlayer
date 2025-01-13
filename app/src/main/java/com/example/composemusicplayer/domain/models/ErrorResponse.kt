package com.example.composemusicplayer.domain.models

/**
 * Модель ошибки с сервера. Запрещено изменять названия полей
 */
data class ErrorResponse(
    val error_message: String,
    val details: String?,
    val message: String?,
    val status: Int
)
