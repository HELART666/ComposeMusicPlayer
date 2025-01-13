package com.example.composemusicplayer.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.example.composemusicplayer.R
import com.example.composemusicplayer.calculateProgress
import com.example.composemusicplayer.domain.models.TrackServer
import com.example.composemusicplayer.getTiming
import com.example.composemusicplayer.presentation.playerService.PlaybackService
import com.example.composemusicplayer.presentation.viewmodels.TrackListViewModel
import kotlinx.coroutines.delay

@Composable
fun MainTrackScreenBottomSheet(
    viewModel: TrackListViewModel,
    track: TrackServer,
    startIconEnable: Boolean,
    screenTitle: String,
    allTracksIconEnable: Boolean,
    modifier: Modifier,
    player: PlaybackService
) {
    var currentProgress by remember {
        mutableLongStateOf(0L)
    }
    var isPlayingState by remember {
        mutableStateOf(false)
    }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingState = isPlaying
            }
        }
        player.player?.addListener(listener)
        onDispose {
            player.player?.removeListener(listener)
        }
    }

    if (isPlayingState) {
        LaunchedEffect(Unit) {
            while (true) {
                currentProgress = player.player?.currentPosition ?: 0L
                delay(1000)
            }
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                startIconEnable = startIconEnable,
                screenTitle = screenTitle,
                allTracksIconEnable = allTracksIconEnable,
            )
        }
    ) { innerPadding ->
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(500.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .padding(innerPadding)
            )
            player.player?.let {
                Slider(
                    value = calculateProgress(currentProgress, it.contentDuration),
                    onValueChange = { newProgress ->
                        player.player?.seekTo((newProgress * it.contentDuration).toLong())
                        currentProgress = newProgress.toLong()
                    },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = getTiming(currentProgress))
                Text(text = player.player?.contentDuration?.let { getTiming(it) } ?: "00:00")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    player.player?.seekBack()
                }) {
                    Text(text = "Prev")
                }
                if (isPlayingState) {
                    Button(onClick = {
                        player.player?.pause()
                    }) {
                        Text(text = "Pause")
                    }
                } else {
                    Button(onClick = {
                        player.player?.play()
                    }) {
                        Text(text = "Play")
                    }
                }
                Button(onClick = {

                }) {
                    Text(text = "Next")
                }
            }
        }
    }
}