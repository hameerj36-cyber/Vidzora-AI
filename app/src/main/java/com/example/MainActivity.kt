package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.VidzoraViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: VidzoraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var isAuthenticated by remember { mutableStateOf(false) }

                if (!isAuthenticated) {
                    AuthScreen(onLoginSuccess = { isAuthenticated = true })
                } else {
                    VidzoraApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun VidzoraApp(viewModel: VidzoraViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val selectedVideo by viewModel.selectedVideo.collectAsStateWithLifecycle()
    val processingStatus by viewModel.processingStatus.collectAsStateWithLifecycle()
    val processingProgress by viewModel.processingProgress.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    val showBottomNav = currentScreen in listOf("home", "create", "my_videos", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen == "home",
                        onClick = { viewModel.navigateTo("home") },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == "create",
                        onClick = { viewModel.navigateTo("create") },
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create") },
                        label = { Text("Create") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == "my_videos",
                        onClick = { viewModel.navigateTo("my_videos") },
                        icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "My Videos") },
                        label = { Text("Library") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == "profile",
                        onClick = { viewModel.navigateTo("profile") },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "home" -> HomeScreen(
                    videos = videos,
                    onCreateShort = { viewModel.navigateTo("short") },
                    onCreateVlog = { viewModel.navigateTo("vlog") },
                    onAiImage = { viewModel.navigateTo("ai_image") },
                    onVideoClick = { video ->
                        viewModel.selectVideo(video)
                        viewModel.navigateTo("result")
                    }
                )
                "create" -> CreateScreen(
                    onNavigateMode = { mode -> viewModel.navigateTo(mode) }
                )
                "short" -> ShortGeneratorScreen(
                    onBack = { viewModel.navigateTo("home") },
                    onGenerate = { req ->
                        viewModel.generateVideo(req) { video ->
                            viewModel.navigateTo("result")
                        }
                    }
                )
                "vlog" -> VlogGeneratorScreen(
                    onBack = { viewModel.navigateTo("home") },
                    onGenerate = { req ->
                        viewModel.generateVideo(req) { video ->
                            viewModel.navigateTo("result")
                        }
                    }
                )
                "text_to_video" -> TextToVideoScreen(
                    onBack = { viewModel.navigateTo("create") },
                    onGenerate = { req ->
                        viewModel.generateVideo(req) { video ->
                            viewModel.navigateTo("result")
                        }
                    }
                )
                "image_to_video" -> ImageToVideoScreen(
                    onBack = { viewModel.navigateTo("create") },
                    onGenerate = { req ->
                        viewModel.generateVideo(req) { video ->
                            viewModel.navigateTo("result")
                        }
                    }
                )
                "video_capture" -> VideoCaptureScreen(
                    onBack = { viewModel.navigateTo("create") },
                    onFootageRecorded = { uri ->
                        viewModel.navigateTo("image_to_video")
                    }
                )
                "ai_image" -> AiImageGeneratorScreen(
                    onBack = { viewModel.navigateTo("home") },
                    onUseForVideo = { imgUrl ->
                        viewModel.navigateTo("image_to_video")
                    }
                )
                "processing" -> ProcessingScreen(
                    status = processingStatus,
                    progress = processingProgress
                )
                "result" -> VideoResultScreen(
                    video = selectedVideo,
                    onBack = { viewModel.navigateTo("home") },
                    onRegenerate = {
                        selectedVideo?.let { v ->
                            viewModel.navigateTo("processing")
                            viewModel.generateVideo(
                                com.example.model.VideoGenerationRequest(
                                    prompt = v.prompt,
                                    mode = v.type,
                                    duration = v.duration,
                                    aspectRatio = v.aspectRatio,
                                    style = v.style
                                )
                            ) { viewModel.navigateTo("result") }
                        }
                    }
                )
                "my_videos" -> MyVideosScreen(
                    videos = videos,
                    onVideoClick = { video ->
                        viewModel.selectVideo(video)
                        viewModel.navigateTo("result")
                    },
                    onDelete = { id -> viewModel.deleteVideo(id) },
                    onRename = { id, title -> viewModel.renameVideo(id, title) }
                )
                "profile" -> ProfileScreen(
                    userProfile = userProfile,
                    onOpenAdmin = { viewModel.navigateTo("admin") }
                )
                "admin" -> AdminDashboardScreen(
                    adminData = viewModel.getAdminData(),
                    onBack = { viewModel.navigateTo("profile") }
                )
            }
        }
    }
}
