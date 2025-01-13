package com.example.composemusicplayer

import android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.core.app.ActivityCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.example.composemusicplayer.domain.models.TrackServer
import com.example.composemusicplayer.presentation.AppToolbar
import com.example.composemusicplayer.presentation.navigation.RootScreen
import com.example.composemusicplayer.presentation.playerService.PlaybackService
import com.example.composemusicplayer.ui.theme.ComposeMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_MEDIA_AUDIO, FOREGROUND_SERVICE_MEDIA_PLAYBACK, POST_NOTIFICATIONS),
            0
        )
        enableEdgeToEdge()
        setContent {
            ComposeMusicPlayerTheme {
                Scaffold(topBar = {
                    AppToolbar(
                        startIconEnable = false,
                        screenTitle = "Все треки",
                        allTracksIconEnable = false,
                    )
                }) {
                    RootScreen()
                }
            }
        }
    }
}


fun getTiming(milliseconds: Long): String {
    val minutes = milliseconds / 60000
    val seconds = (milliseconds % 60000) / 1000

    return String.format("%02d:%02d", minutes, seconds)
}

@androidx.annotation.OptIn(UnstableApi::class)
fun startPlayer(
    player: PlaybackService,
    trackList: List<TrackServer>,
    position: Int,
    progressiveUri: String,
) {
    player.player?.apply {
        val mediaItem = MediaItem.fromUri(progressiveUri)
        setMediaItem(mediaItem)
        prepare()
        play()
    }
}


fun calculateProgress(currentTime: Long, duration: Long): Float {
    return if (duration > 0) {
        (currentTime.toFloat() / duration).coerceIn(0f, 1f)
    } else 0f
}

