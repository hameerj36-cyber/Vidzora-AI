package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ImageGenerationRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiImageGeneratorScreen(
    onBack: () -> Unit,
    onUseForVideo: (String) -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("Cinematic") }
    var aspectRatio by remember { mutableStateOf("9:16") }
    var generatedImageUrl by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }

    val styles = listOf("Cinematic", "Realistic", "3D Anime", "Cyberpunk", "Fantasy", "Watercolor")
    val ratios = listOf("9:16", "16:9", "1:1")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Image Generator") },
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Image Prompt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("A futuristic floating castle in the clouds surrounded by glowing energy rings...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OptSelector(items = styles, selectedItem = style) { style = it }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Aspect Ratio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ratios.forEach { ratio ->
                        FilterChip(
                            selected = aspectRatio == ratio,
                            onClick = { aspectRatio = ratio },
                            label = { Text(ratio) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    isGenerating = true
                    generatedImageUrl = "https://picsum.photos/seed/" + prompt.hashCode() + "/800/1200"
                    isGenerating = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Image", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (generatedImageUrl != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Generated Result", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        ) {
                            // preview placeholder
                        }
                        Button(
                            onClick = { onUseForVideo(generatedImageUrl!!) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.Movie, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Use Image for Video", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
