package com.example.composemusicplayer.domain.models

import android.graphics.Bitmap

data class Track(
    val artistName: String?,
    val trackName: String,
    val timing: Int,
    val uri: String,
    val cover: Bitmap?
)
