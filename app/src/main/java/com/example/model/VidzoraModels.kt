package com.example.model

data class VideoItem(
    val id: String,
    val title: String,
    val prompt: String,
    val type: String, // "SHORT", "VLOG", "IMAGE"
    val duration: Int, // e.g. 60 seconds max
    val aspectRatio: String, // "9:16", "16:9", "1:1"
    val style: String,
    val status: String, // "QUEUED", "GENERATING", "PROCESSING", "RENDERING", "FINALIZING", "COMPLETED", "FAILED"
    val progress: Int = 100,
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class VideoGenerationRequest(
    val prompt: String,
    val mode: String, // "SHORT" or "VLOG"
    val duration: Int = 60, // Default and max 60 seconds
    val aspectRatio: String, // 9:16 for short, 16:9 for vlog
    val style: String,
    val cameraMovement: String? = null,
    val audio: String? = null,
    val voice: String? = null,
    val imageInput: String? = null,
    val scenes: List<String>? = null,
    val script: String? = null
)

data class ImageGenerationRequest(
    val prompt: String,
    val style: String,
    val aspectRatio: String // "9:16", "16:9", "1:1"
)

data class UserProfile(
    val userId: String,
    val email: String,
    val name: String,
    val plan: String, // "Free Plan" or "Pro Plan"
    val videoGenerationsRemaining: Int,
    val imageGenerationsRemaining: Int,
    val isPro: Boolean
)

data class AdminDashboardData(
    val totalUsers: Int,
    val totalGenerations: Int,
    val failedJobsCount: Int,
    val aiProviderStatus: String, // "Operational", "Degraded"
    val activeStorageMb: Long,
    val moderationQueueCount: Int
)
