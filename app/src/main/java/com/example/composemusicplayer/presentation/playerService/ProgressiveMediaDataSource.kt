package com.example.composemusicplayer.presentation.playerService

import androidx.media3.exoplayer.source.MediaSource


class ProgressiveMediaSourceFactory {
    fun createMediaSource(uri: String): MediaSource {
        return ProgressiveMediaSourceFactory().createMediaSource(uri)
    }
}