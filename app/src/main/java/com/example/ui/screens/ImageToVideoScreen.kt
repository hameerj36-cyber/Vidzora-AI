package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoGenerationRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageToVideoScreen(
    onBack: () -> Unit,
    onGenerate: (VideoGenerationRequest) -> Unit
) {
    var animationPrompt by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("SHORT") }
    var duration by remember { mutableIntStateOf(60) }
    var imageSelected by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image-to-Video Animation") },
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
            // Image picker box
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select Source Image", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (imageSelected) "Image attached (sample_artwork.jpg)" else "Tap to upload or pick image",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Animation Prompt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = animationPrompt,
                    onValueChange = { animationPrompt = it },
                    placeholder = { Text("Slowly move the camera forward while the clouds move naturally and the lights turn on.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Mode & Aspect Ratio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("SHORT" to "Short (9:16)", "VLOG" to "Vlog (16:9)").forEach { (m, label) ->
                        val isSelected = mode == m
                        Button(
                            onClick = { mode = m },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(label, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Duration (Max 60s)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(10, 20, 30, 60).forEach { d ->
                        FilterChip(
                            selected = duration == d,
                            onClick = { duration = d },
                            label = { Text("${d}s") }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val p = animationPrompt.ifBlank { "Slowly move camera forward while clouds move naturally." }
                    onGenerate(
                        VideoGenerationRequest(
                            prompt = p,
                            mode = mode,
                            duration = duration,
                            aspectRatio = if (mode == "SHORT") "9:16" else "16:9",
                            style = "Cinematic",
                            imageInput = "sample_artwork.jpg"
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Movie, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GENERATE ANIMATED VIDEO (${duration}s)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
