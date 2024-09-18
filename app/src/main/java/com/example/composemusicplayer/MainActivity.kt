package com.example.composemusicplayer

import android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composemusicplayer.domain.models.Track
import com.example.composemusicplayer.domain.models.toMediaItem
import com.example.composemusicplayer.presentation.playerService.PlaybackService
import com.example.composemusicplayer.presentation.viewmodels.TrackListViewModel
import com.example.composemusicplayer.ui.theme.ComposeMusicPlayerTheme
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TrackListViewModel by viewModels()

    private val service = PlaybackService()

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            service.player = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }


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
                Scaffold { innerPadding ->
                    AppToolbar(
                        startIconEnable = false,
                        screenTitle = "Все треки",
                        allTracksIconEnable = false,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                    MainTrackListScreen(
                        viewModel,
                        modifier = Modifier
                            .padding(innerPadding),
                        player = service,
                        context = this
                    )
                }
            }
        }
    }
}

@Composable
fun MainTrackListScreen(
    viewModel: TrackListViewModel,
    modifier: Modifier,
    player: PlaybackService,
    context: Context,
) {
    val trackList = viewModel.trackListState.collectAsState().value
    val playerDialogState = viewModel.playerDialogState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getAllTracks()
    }

    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(trackList) { index, track ->
            TrackCard(
                icon = track.cover,
                trackName = track.trackName,
                artistName = track.artistName,
                uri = track.uri.toUri(),
                timing = getTiming(track.timing.toLong()),
                onClick = {
                    startPlayer(
                        player,
                        trackList,
                        index
                    )
                },
            )
        }
    }
}

@Composable
fun AppToolbar(
    startIconEnable: Boolean,
    screenTitle: String,
    allTracksIconEnable: Boolean,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (startIconEnable) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null
            )
        }
        Text(
            text = screenTitle,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = Bold
            )
        )
        if (allTracksIconEnable) {
            Icon(
                painter = painterResource(id = R.drawable.ic_track_list),
                contentDescription = null
            )
        }
    }
}

@Composable
fun MainTrackScreenBottomSheet(
    trackImage: ImageBitmap?,
    trackName: String,
    artistName: String,
    timing: String,
    startIconEnable: Boolean,
    screenTitle: String,
    allTracksIconEnable: Boolean,
    modifier: Modifier,
) {
    Scaffold(
        topBar = {
            AppToolbar(
                startIconEnable = startIconEnable,
                screenTitle = screenTitle,
                allTracksIconEnable = allTracksIconEnable,
                modifier = modifier
            )
        }
    ) { innerPadding ->
        Text(
            modifier = Modifier
                .padding(innerPadding),
            text = "Keks"
        )
    }
}

@Composable
fun TrackCard(
    icon: Bitmap?,
    trackName: String,
    artistName: String?,
    uri: Uri,
    timing: String,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick()
            }
    ) {
        if (icon == null) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .size(75.dp)
            )
        } else {
            Image(
                bitmap = icon.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .size(75.dp)
            )
        }
        Column {
            Text(
                text = trackName
            )
            Text(
                text = artistName ?: "Unknown"
            )
        }
        Text(
            text = timing
        )
    }
}

fun getTiming(milliseconds: Long): String {
    val minutes = milliseconds / 60000
    val seconds = (milliseconds % 60000) / 1000

    return String.format("%02d:%02d", minutes, seconds)
}

fun startPlayer(
    player: PlaybackService,
    trackList: List<Track>,
    position: Int
) {
    player.player?.apply {
        println("notnull")
        setMediaItems(
            trackList.toMediaItems(), position, 0L)
        prepare()
        play()
    }
}

fun List<Track>.toMediaItems(): List<MediaItem> {
    return map { track -> track.toMediaItem() }
}
