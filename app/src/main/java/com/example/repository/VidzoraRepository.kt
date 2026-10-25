package com.example.repository

import com.example.model.*
import com.example.providers.GeminiImageProvider
import com.example.providers.GeminiVideoProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VidzoraRepository {
    private val videoProvider = GeminiVideoProvider()
    private val imageProvider = GeminiImageProvider()

    private val _videos = MutableStateFlow<List<VideoItem>>(
        listOf(
            VideoItem(
                id = "vid_1",
                title = "Cyberpunk City Sunset Cinematic",
                prompt = "Cinematic gaming-style video of a futuristic Minecraft-inspired city at sunset.",
                type = "SHORT",
                duration = 60,
                aspectRatio = "9:16",
                style = "Cinematic",
                status = "COMPLETED",
                videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "https://picsum.photos/seed/cyberpunk/400/600",
                createdAt = System.currentTimeMillis() - 3600000
            ),
            VideoItem(
                id = "vid_2",
                title = "Tokyo Travel Vlog Exploration",
                prompt = "A one-minute cinematic vlog about exploring a futuristic city in Tokyo.",
                type = "VLOG",
                duration = 60,
                aspectRatio = "16:9",
                style = "Travel vlog",
                status = "COMPLETED",
                videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                thumbnailUrl = "https://picsum.photos/seed/tokyo/600/400",
                createdAt = System.currentTimeMillis() - 7200000
            )
        )
    )
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            userId = "usr_99281",
            email = "creator@vidzora.ai",
            name = "Alex Creator",
            plan = "Pro Plan",
            videoGenerationsRemaining = 45,
            imageGenerationsRemaining = 120,
            isPro = true
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    suspend fun generateVideo(request: VideoGenerationRequest): Result<VideoItem> {
        // Content safety check
        if (request.prompt.contains("unsafe", ignoreCase = true) || request.prompt.contains("harm", ignoreCase = true)) {
            return Result.failure(Exception("Request violates safety policies and content moderation guidelines."))
        }
        
        val newVideo = videoProvider.generateVideo(request)
        val updated = mutableListOf(newVideo)
        updated.addAll(_videos.value)
        _videos.value = updated
        
        // Deduct remaining credits
        val current = _userProfile.value
        if (current.videoGenerationsRemaining > 0) {
            _userProfile.value = current.copy(videoGenerationsRemaining = current.videoGenerationsRemaining - 1)
        }
        
        return Result.success(newVideo)
    }

    suspend fun generateImage(request: ImageGenerationRequest): Result<String> {
        if (request.prompt.contains("unsafe", ignoreCase = true)) {
            return Result.failure(Exception("Prompt violates content moderation rules."))
        }
        val url = imageProvider.generateImage(request.prompt, request.style, request.aspectRatio)
        val current = _userProfile.value
        if (current.imageGenerationsRemaining > 0) {
            _userProfile.value = current.copy(imageGenerationsRemaining = current.imageGenerationsRemaining - 1)
        }
        return Result.success(url)
    }

    fun deleteVideo(id: String) {
        _videos.value = _videos.value.filter { it.id != id }
    }

    fun renameVideo(id: String, newTitle: String) {
        _videos.value = _videos.value.map {
            if (it.id == id) it.copy(title = newTitle) else it
        }
    }

    fun getAdminData(): AdminDashboardData {
        return AdminDashboardData(
            totalUsers = 14250,
            totalGenerations = 89420,
            failedJobsCount = 12,
            aiProviderStatus = "Operational",
            activeStorageMb = 482910,
            moderationQueueCount = 0
        )
    }
}
