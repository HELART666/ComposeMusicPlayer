package com.example.composemusicplayer.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.composemusicplayer.domain.models.Track
import com.example.composemusicplayer.domain.models.TrackServer
import com.example.composemusicplayer.state.ScreenState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface TrackListRepository {
    suspend fun getAllTracks(): Flow<Track>

    suspend fun getAllTracksByServer(): ScreenState<List<TrackServer>>

    suspend fun sendTrackToServer(
        multipartBody: MultipartBody.Part,
        userId: MultipartBody.Part,
    ): ScreenState<Unit>

    fun getAlbumArt(path: Uri): Bitmap?
}