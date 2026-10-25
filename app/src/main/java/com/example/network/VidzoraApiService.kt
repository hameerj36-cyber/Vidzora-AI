package com.example.network

import com.example.model.*
import retrofit2.Response
import retrofit2.http.*

interface VidzoraApiService {

    @POST("api/v1/video/generate")
    suspend fun generateVideo(@Body request: VideoGenerationRequest): Response<VideoItem>

    @GET("api/v1/video/generations/{id}")
    suspend fun getGenerationStatus(@Path("id") id: String): Response<VideoItem>

    @POST("api/v1/video/generations/{id}/cancel")
    suspend fun cancelGeneration(@Path("id") id: String): Response<Unit>

    @GET("api/v1/videos")
    suspend fun getVideos(@Query("category") category: String? = null): Response<List<VideoItem>>

    @DELETE("api/v1/videos/{id}")
    suspend fun deleteVideo(@Path("id") id: String): Response<Unit>

    @POST("api/v1/image/generate")
    suspend fun generateImage(@Body request: ImageGenerationRequest): Response<Map<String, String>>

    @POST("api/v1/image-to-video")
    suspend fun imageToVideo(@Body request: VideoGenerationRequest): Response<VideoItem>

    @GET("api/v1/user/profile")
    suspend fun getUserProfile(): Response<UserProfile>

    @GET("api/v1/user/usage")
    suspend fun getUserUsage(): Response<Map<String, Any>>
}
