package com.example.providers

import com.example.model.VideoGenerationRequest
import com.example.model.VideoItem
import kotlinx.coroutines.delay

interface VideoGenerationProvider {
    suspend fun generateVideo(request: VideoGenerationRequest): VideoItem
    suspend fun checkStatus(generationId: String): VideoItem
    suspend fun cancelGeneration(generationId: String): Boolean
}

interface ImageGenerationProvider {
    suspend fun generateImage(prompt: String, style: String, aspectRatio: String): String
}

class GeminiVideoProvider : VideoGenerationProvider {
    override suspend fun generateVideo(request: VideoGenerationRequest): VideoItem {
        // Simulates backend call to real AI video generation provider upholding the 60-second requirement
        delay(1500)
        val id = "gen_" + System.currentTimeMillis()
        return VideoItem(
            id = id,
            title = request.prompt.take(30) + if (request.prompt.length > 30) "..." else "",
            prompt = request.prompt,
            type = request.mode,
            duration = request.duration,
            aspectRatio = request.aspectRatio,
            style = request.style,
            status = "COMPLETED",
            progress = 100,
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://picsum.photos/seed/$id/400/600",
            createdAt = System.currentTimeMillis()
        )
    }

    override suspend fun checkStatus(generationId: String): VideoItem {
        delay(300)
        return VideoItem(
            id = generationId,
            title = "Generated Video",
            prompt = "Sample prompt",
            type = "SHORT",
            duration = 60,
            aspectRatio = "9:16",
            style = "Cinematic",
            status = "COMPLETED",
            progress = 100,
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://picsum.photos/seed/$generationId/400/600"
        )
    }

    override suspend fun cancelGeneration(generationId: String): Boolean {
        delay(200)
        return true
    }
}

class GeminiImageProvider : ImageGenerationProvider {
    override suspend fun generateImage(prompt: String, style: String, aspectRatio: String): String {
        delay(1000)
        return "https://picsum.photos/seed/" + prompt.hashCode() + "/800/1200"
    }
}
