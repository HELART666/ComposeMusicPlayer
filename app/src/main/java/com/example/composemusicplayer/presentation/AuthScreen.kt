package com.example.composemusicplayer.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composemusicplayer.domain.models.AuthRequest
import com.example.composemusicplayer.presentation.viewmodels.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    navToPicker: () -> Unit,
) {
    val login = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(top = 120.dp)
        ) {
            TextField(
                value = login.value,
                onValueChange = { text ->
                    login.value = text
                }
            )
            TextField(
                value = password.value,
                onValueChange = { text ->
                    password.value = text
                }
            )
            Button(
                onClick = {
                    viewModel.auth(
                        AuthRequest(
                            email = login.value,
                            password = password.value
                        )
                    ) {
                        navToPicker()
                    }
                }
            ) {
                Text(
                    text = "Войти"
                )
            }
        }
    }
}