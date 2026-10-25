package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.VideoCameraFront
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
fun VlogGeneratorScreen(
    onBack: () -> Unit,
    onGenerate: (VideoGenerationRequest) -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableIntStateOf(60) } // Maximum 60 seconds
    var selectedStyle by remember { mutableStateOf("Cinematic vlog") }
    var script by remember { mutableStateOf("") }
    
    var scene1 by remember { mutableStateOf("Scene 1 — Introduction: Establishing shot of futuristic city.") }
    var scene2 by remember { mutableStateOf("Scene 2 — Main activity: Exploring neon tech markets.") }
    var scene3 by remember { mutableStateOf("Scene 3 — Interesting moment: Hover-bike street show.") }
    var scene4 by remember { mutableStateOf("Scene 4 — Ending: Sunset view from skyscraper rooftop.") }

    val styles = listOf("Travel vlog", "Gaming vlog", "Daily vlog", "Cinematic vlog", "Adventure", "Documentary", "Lifestyle", "Story vlog")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vlog Generator (16:9 Landscape)") },
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
            // Notice about 60s max
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VideoCameraFront, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Vlog mode is capped at 60 seconds maximum for short dynamic storytelling.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Prompt
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Vlog Prompt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Create a one-minute cinematic vlog about exploring a futuristic city.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // Duration
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Duration (Max 60 Seconds)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
                            Text("${dur}s", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Aspect Ratio 16:9
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Aspect Ratio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "16:9 Landscape (Automatically set for Vlogs)",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Style
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Vlog Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OptSelector(items = styles, selectedItem = selectedStyle) { selectedStyle = it }
            }

            // AI Script Button & Box
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("AI Script & Voiceover", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedButton(
                        onClick = {
                            script = "Welcome back to the channel! Today we're exploring the glowing neon streets of New Neo City. Check out this incredible architecture..."
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate Vlog Script")
                    }
                }
                if (script.isNotBlank()) {
                    OutlinedTextField(
                        value = script,
                        onValueChange = { script = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Optional Scenes Editor
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("60-Second Scene Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(value = scene1, onValueChange = { scene1 = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = scene2, onValueChange = { scene2 = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = scene3, onValueChange = { scene3 = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = scene4, onValueChange = { scene4 = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Generate Button
            Button(
                onClick = {
                    val finalPrompt = prompt.ifBlank { "One-minute cinematic vlog about exploring a futuristic city." }
                    onGenerate(
                        VideoGenerationRequest(
                            prompt = finalPrompt,
                            mode = "VLOG",
                            duration = selectedDuration,
                            aspectRatio = "16:9",
                            style = selectedStyle,
                            script = script,
                            scenes = listOf(scene1, scene2, scene3, scene4)
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary)
            ) {
                Icon(Icons.Default.VideoCameraFront, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedDuration == 60) "GENERATE 60-SECOND VLOG" else "GENERATE ${selectedDuration}-SECOND VLOG",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
