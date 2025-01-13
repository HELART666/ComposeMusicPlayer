package com.example.composemusicplayer.presentation

import android.content.ComponentName
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composemusicplayer.getTiming
import com.example.composemusicplayer.presentation.playerService.PlaybackService
import com.example.composemusicplayer.presentation.viewmodels.TrackListViewModel
import com.example.composemusicplayer.startPlayer
import com.google.common.util.concurrent.MoreExecutors

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTrackListScreen(
    viewModel: TrackListViewModel = hiltViewModel<TrackListViewModel>(),
    navToUpload: () -> Unit
) {
    val trackListServer = viewModel.trackListServerState.collectAsState().value
    val playerDialogState = viewModel.playerDialogState.collectAsState().value
    val currentTrackState = viewModel.currentTrackState.collectAsState().value

    val service = PlaybackService()
    val context = LocalContext.current
    val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
    val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
    controllerFuture.addListener({
        service.player = controllerFuture.get()
    }, MoreExecutors.directExecutor())

    Box(Modifier.fillMaxSize()) {
        CurrentTrackStateComponent(
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.BottomCenter),
            fileName = currentTrackState?.fileName.toString()
        )
        Column(
            modifier = Modifier
                .padding(top = 120.dp)
        ) {
            Button(onClick = {
                navToUpload()
            }) {
                Text("Загрузить трек")
            }

            if (playerDialogState) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.changeDialogState() },
                ) {
                    currentTrackState?.apply {
                        MainTrackScreenBottomSheet(
                            viewModel = viewModel,
                            track = currentTrackState,
                            startIconEnable = true,
                            screenTitle = "Сейчас играет",
                            allTracksIconEnable = true,
                            modifier = Modifier,
                            player = service
                        )
                    }
                }
            }

            LazyColumn {
                items(trackListServer) { track ->
                    TrackCard(
                        icon = null,
                        trackName = track.fileName,
                        timing = getTiming(
                            service.player?.currentMediaItem?.mediaMetadata?.durationMs ?: 0L
                        ),
                        onClick = {
                            startPlayer(
                                service,
                                trackListServer,
                                0,
                                "http://192.168.74.92:8080/api/v1/files/stream/${track.fileName}"
                            )
                            viewModel.setCurrentTrackState(track)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentTrackStateComponent(
    viewModel: TrackListViewModel,
    modifier: Modifier,
    fileName: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .border(width = 1.dp, color = Color.Gray, RoundedCornerShape(12.dp))
            .clickable { viewModel.changeDialogState() }
    ) {
        Text(
            fileName,
        )
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play icon",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}