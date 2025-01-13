package com.example.composemusicplayer.state

sealed class ScreenState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?): ScreenState<T>(data)
    class Error<T>(data: T?, message: String?): ScreenState<T>(data, message)
    class Loading<T>(): ScreenState<T>()
}