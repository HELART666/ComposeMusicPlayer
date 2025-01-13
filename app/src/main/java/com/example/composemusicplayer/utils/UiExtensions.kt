package com.example.composemusicplayer.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File

@SuppressLint("Range")
fun getFileName(uri: Uri, context: Context): String? {
    val scheme = uri.scheme
    return when (scheme) {
        ContentResolver.SCHEME_FILE -> uri.lastPathSegment
        ContentResolver.SCHEME_CONTENT ->
            try {
                context.contentResolver.query(uri,
                    arrayOf(OpenableColumns.DISPLAY_NAME),
                    null, null, null)?.use { cursor ->
                    if (cursor.count > 0 && cursor.moveToFirst()) {
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    } else {
                        ""
                    }
                } ?: ""
            } catch (e: Exception) {
                Log.e("MainActivity", "Error getting file name", e)
                ""
            }
        else -> uri.path?.let { File(it).name } ?: ""
    }
}