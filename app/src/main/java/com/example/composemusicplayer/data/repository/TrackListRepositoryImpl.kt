package com.example.composemusicplayer.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.example.composemusicplayer.domain.models.Track
import com.example.composemusicplayer.domain.repository.TrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class TrackListRepositoryImpl @Inject constructor(
    private val applicationContext: Context,
    private val mediaMetadataRetriever: MediaMetadataRetriever
) : TrackListRepository {
    override suspend fun getAllTracks(): Flow<Track> {

        val audioFiles = mutableListOf<Track>()
        val selection =
            "((${MediaStore.Audio.Media.IS_MUSIC} != 0) OR (${MediaStore.Audio.Media.IS_RINGTONE} != 0)) AND (${MediaStore.Audio.Media.MIME_TYPE} LIKE '%audio/mpeg%')"
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
            ),
            selection,
            null,
            sortOrder
        ) ?: return emptyList<Track>().asFlow()

        try {
            while (cursor.moveToNext()) {
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artistName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val timing =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val filePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val cover = getAlbumArt(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)).toUri())

                audioFiles.add(
                    Track(
                        artistName = artistName,
                        trackName = title,
                        timing = timing,
                        uri = filePath,
                        cover = cover
                    )
                )
            }
        } finally {
            cursor.close()
        }
        return audioFiles.asFlow()
    }

    override fun getAlbumArt(path: Uri): Bitmap? {
        mediaMetadataRetriever.setDataSource(applicationContext, path)
        val cover = mediaMetadataRetriever.embeddedPicture
        cover?.let {
            return BitmapFactory.decodeByteArray(cover, 0, cover.size)
        }
        return null
    }
}