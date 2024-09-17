package com.example.composemusicplayer.di

import android.media.MediaMetadataRetriever
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideMediaRetriever() = MediaMetadataRetriever()
}