package com.example.composemusicplayer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemusicplayer.domain.models.TrackServer
import com.example.composemusicplayer.domain.usecases.GetAllTracksUseCase
import com.example.composemusicplayer.domain.usecases.TrackFromServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(
    private val getAllTracksUseCase: GetAllTracksUseCase,
    private val trackFromServerUseCase: TrackFromServerUseCase,
) : ViewModel() {

    private val _currentTrackState = MutableStateFlow<TrackServer?>(null)
    val currentTrackState = _currentTrackState.asStateFlow()
    private val _playerDialogState = MutableStateFlow(false)
    val playerDialogState = _playerDialogState.asStateFlow()

    private val _trackListServerState = MutableStateFlow<List<TrackServer>>(emptyList())
    val trackListServerState = _trackListServerState.asStateFlow()

    init {
        getAllTracksFromServer()
    }

    private fun getAllTracksFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            trackFromServerUseCase.getAllTracks().let {
                it.data?.let { tracks ->
                    _trackListServerState.value = tracks
                }
            }
        }
    }

    suspend fun uploadTrack(
        multipartBody: MultipartBody.Part,
        userId: MultipartBody.Part,
        onSuccess: () -> Unit
    ) {
        trackFromServerUseCase.uploadTrack(multipartBody, userId).let {
            println(it.data)
            it.data?.let {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
            println(it.message)
        }
    }

    fun changeDialogState() {
        _playerDialogState.value = !_playerDialogState.value
    }

    fun setCurrentTrackState(track: TrackServer) {
        _currentTrackState.value = track
    }
}