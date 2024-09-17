package com.example.composemusicplayer.domain.usecases

import com.example.composemusicplayer.domain.repository.TrackListRepository
import javax.inject.Inject

class GetAllTracksUseCase @Inject constructor(
    private val repository: TrackListRepository
) {
    suspend operator fun invoke() = repository.getAllTracks()
}