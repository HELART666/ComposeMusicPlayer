package com.example.composemusicplayer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.composemusicplayer.domain.models.Track
import com.example.composemusicplayer.domain.usecases.GetAllTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(
    private val getAllTracksUseCase: GetAllTracksUseCase
) : ViewModel() {
    private val _trackListState = MutableStateFlow<List<Track>>(emptyList())
    val trackListState = _trackListState.asStateFlow()
    private val _playerDialogState = MutableStateFlow(false)
    val playerDialogState = _playerDialogState.asStateFlow()

    suspend fun getAllTracks() {
        val tracks = mutableListOf<Track>()
        getAllTracksUseCase.invoke().collect {
            tracks.add(it)
        }
        _trackListState.value = tracks
    }

    fun changeDialogState() {
        _playerDialogState.value = !_playerDialogState.value
    }
}