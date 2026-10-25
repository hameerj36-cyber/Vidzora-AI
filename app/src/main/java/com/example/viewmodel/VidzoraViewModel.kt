package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import com.example.repository.VidzoraRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VidzoraViewModel : ViewModel() {
    private val repository = VidzoraRepository()

    val videos = repository.videos
    val userProfile = repository.userProfile

    private val _currentScreen = MutableStateFlow("home")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedVideo = MutableStateFlow<VideoItem?>(null)
    val selectedVideo: StateFlow<VideoItem?> = _selectedVideo.asStateFlow()

    // Processing status state machine for generation
    private val _processingStatus = MutableStateFlow<String>("Queued")
    val processingStatus: StateFlow<String> = _processingStatus.asStateFlow()

    private val _processingProgress = MutableStateFlow<Int>(0)
    val processingProgress: StateFlow<Int> = _processingProgress.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectVideo(video: VideoItem?) {
        _selectedVideo.value = video
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun generateVideo(request: VideoGenerationRequest, onComplete: (VideoItem) -> Unit) {
        viewModelScope.launch {
            _processingStatus.value = "Queued"
            _processingProgress.value = 5
            _currentScreen.value = "processing"

            delay(600)
            _processingStatus.value = "Generating"
            _processingProgress.value = 25

            delay(800)
            _processingStatus.value = "Processing"
            _processingProgress.value = 50

            delay(800)
            _processingStatus.value = "Rendering 60s Clip"
            _processingProgress.value = 75

            delay(600)
            _processingStatus.value = "Finalizing"
            _processingProgress.value = 95

            val result = repository.generateVideo(request)
            result.fold(
                onSuccess = { video ->
                    _processingStatus.value = "Completed"
                    _processingProgress.value = 100
                    delay(400)
                    _selectedVideo.value = video
                    onComplete(video)
                },
                onFailure = { error ->
                    _errorMessage.value = error.message ?: "Generation failed"
                    _currentScreen.value = "home"
                }
            )
        }
    }

    fun deleteVideo(id: String) {
        repository.deleteVideo(id)
    }

    fun renameVideo(id: String, newTitle: String) {
        repository.renameVideo(id, newTitle)
    }

    fun getAdminData(): AdminDashboardData {
        return repository.getAdminData()
    }
}
