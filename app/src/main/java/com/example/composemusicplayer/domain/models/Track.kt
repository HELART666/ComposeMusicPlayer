package com.example.composemusicplayer.domain.models

import android.graphics.Bitmap
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession.MediaItemsWithStartPosition
import kotlinx.serialization.SerialName

data class Track(
    val artistName: String?,
    val trackName: String,
    val timing: Int,
    val uri: String,
    val cover: Bitmap?
)

fun Track.toMediaItem(): MediaItem {
    return MediaItem.fromUri(uri)
}

data class TrackServer(
    @SerialName("id")
    val id: Long,
    @SerialName("fileName")
    val fileName: String,
    @SerialName("userId")
    val userId: Long,
    @SerialName("filePath")
    val filePath: String,
)