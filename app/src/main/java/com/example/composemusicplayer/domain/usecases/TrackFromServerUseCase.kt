package com.example.composemusicplayer.domain.usecases

import com.example.composemusicplayer.domain.repository.TrackListRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class TrackFromServerUseCase @Inject constructor(
    private val repository: TrackListRepository
) {
    suspend fun uploadTrack(
        multipartBody: MultipartBody.Part,
        userId: MultipartBody.Part,
    ) =
        repository.sendTrackToServer(multipartBody, userId)

    suspend fun getAllTracks() = repository.getAllTracksByServer()
}