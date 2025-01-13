package com.example.composemusicplayer.data.api

import com.example.composemusicplayer.domain.models.TrackServer
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TrackListApi {

    @Multipart
    @POST("files/upload")
    suspend fun sendTrack(
        @Part track: MultipartBody.Part,
        @Part userId: MultipartBody.Part,
    ): Response<Unit>

    @GET("files")
    suspend fun getAllTracks(): Response<List<TrackServer>>

    companion object {
        fun create(retrofit: Retrofit): TrackListApi {
            return retrofit.create(TrackListApi::class.java)
        }
    }
}