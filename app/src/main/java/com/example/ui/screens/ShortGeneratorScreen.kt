package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoGenerationRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortGeneratorScreen(
    onBack: () -> Unit,
    onGenerate: (VideoGenerationRequest) -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableIntStateOf(60) } // Default and max 60 seconds
    var selectedStyle by remember { mutableStateOf("Cinematic") }
    var selectedCamera by remember { mutableStateOf("Cinematic") }
    var selectedAudio by remember { mutableStateOf("AI Music") }

    val styles = listOf("Realistic", "Cinematic", "3D", "Anime", "Cartoon", "Gaming", "Fantasy", "Sci-Fi", "Documentary")
    val cameras = listOf("Static", "Zoom", "Pan", "Tracking", "Drone", "Handheld", "Cinematic")
    val audios = listOf("AI Music", "Ambient sound", "AI Voice", "No audio")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Short Generator (9:16)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Prompt input
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Prompt",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Describe your video...\nExample: Create a cinematic gaming-style video of a futuristic Minecraft-inspired city at sunset.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // Duration (10, 20, 30, 60 with 60 prominent)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Duration (Max 60 seconds)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                        Text("60s Default & Max", modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 20, 30, 60).forEach { dur ->
                        val isSelected = selectedDuration == dur
                        Button(
                            onClick = { selectedDuration = dur },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text("${dur}s", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // Aspect Ratio (locked to 9:16)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Aspect Ratio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "9:16 Vertical (Automatically set for Shorts)",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Style selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OptSelector(items = styles, selectedItem = selectedStyle) { selectedStyle = it }
            }

            // Camera selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Camera Movement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OptSelector(items = cameras, selectedItem = selectedCamera) { selectedCamera = it }
            }

            // Audio selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Audio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OptSelector(items = audios, selectedItem = selectedAudio) { selectedAudio = it }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Generate Button
            Button(
                onClick = {
                    val finalPrompt = prompt.ifBlank { "Cinematic gaming-style video of a futuristic Minecraft-inspired city at sunset." }
                    onGenerate(
                        VideoGenerationRequest(
                            prompt = finalPrompt,
                            mode = "SHORT",
                            duration = selectedDuration,
                            aspectRatio = "9:16",
                            style = selectedStyle,
                            cameraMovement = selectedCamera,
                            audio = selectedAudio
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Movie, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedDuration == 60) "GENERATE 60-SECOND SHORT" else "GENERATE ${selectedDuration}-SECOND SHORT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
