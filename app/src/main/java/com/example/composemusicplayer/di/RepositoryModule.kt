package com.example.composemusicplayer.di

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.example.composemusicplayer.data.api.AuthApi
import com.example.composemusicplayer.data.api.TrackListApi
import com.example.composemusicplayer.data.repository.AuthRepositoryImpl
import com.example.composemusicplayer.data.repository.TrackListRepositoryImpl
import com.example.composemusicplayer.domain.repository.AuthRepository
import com.example.composemusicplayer.domain.repository.TrackListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesTracksRepository(
        @ApplicationContext context: Context,
        mediaMetadataRetriever: MediaMetadataRetriever,
        trackListApi: TrackListApi
    ): TrackListRepository =
        TrackListRepositoryImpl(context, mediaMetadataRetriever, trackListApi)

    @Provides
    fun provideAuthRepository(
        authApi: AuthApi
    ): AuthRepository = AuthRepositoryImpl(authApi)
}