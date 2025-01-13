package com.example.composemusicplayer.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composemusicplayer.data.api.TokenManager.Companion.ACCESS_TOKEN_KEY
import com.example.composemusicplayer.presentation.AuthScreen
import com.example.composemusicplayer.presentation.FilePickerScreen
import com.example.composemusicplayer.presentation.MainTrackListScreen
import kotlinx.serialization.Serializable

@Composable
fun RootScreen() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val token = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE).getString(
        ACCESS_TOKEN_KEY, null
    )

    NavHost(navController = navController, startDestination = token?.let { TrackList } ?: Auth) {
        composable<Auth> {
            AuthScreen {
                navController.navigate(TrackList)
            }
        }
        composable<TrackList> {
            MainTrackListScreen {
                navController.navigate(FilePicker)
            }
        }
        composable<FilePicker> { FilePickerScreen {
            navController.popBackStack()
        } }
        // Add more destinations similarly.
    }

}

@Serializable
object Auth

@Serializable
object TrackList

@Serializable
object FilePicker