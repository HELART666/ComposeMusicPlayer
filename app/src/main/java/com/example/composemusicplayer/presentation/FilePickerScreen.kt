package com.example.composemusicplayer.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composemusicplayer.data.api.TokenManager
import com.example.composemusicplayer.presentation.viewmodels.TrackListViewModel
import com.example.composemusicplayer.utils.getFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@SuppressLint("Range")
@Composable
fun FilePickerScreen(
    viewModel: TrackListViewModel = hiltViewModel<TrackListViewModel>(),
    onSuccessUpload: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val filename = remember { mutableStateOf("") }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { result ->
            val item = result?.let {
                filename.value = getFileName(it, context).toString()
                context.contentResolver.openInputStream(it)
            }
            val bytes = item?.readBytes()
            if (filename.value.isNotBlank()) {
                val music = MultipartBody.Part.createFormData(
                    "file", filename.value,
                    bytes!!.toRequestBody("audio/mp3".toMediaTypeOrNull(), 0, bytes.size)
                )
                val userIdBody = tokenManager.getUserId().toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val idFormData = MultipartBody.Part.createFormData(
                    "userId", tokenManager.getUserId().toString()
                )
                coroutineScope.launch(Dispatchers.IO) {
                    viewModel.uploadTrack(music, idFormData) {
                        onSuccessUpload()
                    }
                }
            }
            item?.close()
        }
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            launcher.launch("*/*")
        }) {
            Text("Open file")
        }
    }
}