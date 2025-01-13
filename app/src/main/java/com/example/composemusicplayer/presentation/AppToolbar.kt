package com.example.composemusicplayer.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composemusicplayer.R


@Composable
fun AppToolbar(
    startIconEnable: Boolean,
    screenTitle: String,
    allTracksIconEnable: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(top = 64.dp),
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