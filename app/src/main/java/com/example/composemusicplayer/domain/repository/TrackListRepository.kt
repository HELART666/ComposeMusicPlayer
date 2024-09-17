package com.example.composemusicplayer.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.composemusicplayer.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackListRepository {
    suspend fun getAllTracks() : Flow<Track>

    fun getAlbumArt(path: Uri) : Bitmap?
}