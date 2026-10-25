package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoGenerationRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToVideoScreen(
    onBack: () -> Unit,
    onGenerate: (VideoGenerationRequest) -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("SHORT") }
    var duration by remember { mutableIntStateOf(60) }
    var style by remember { mutableStateOf("Cinematic") }

    val styles = listOf("Cinematic", "Realistic", "3D", "Anime", "Cyberpunk", "Fantasy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text-to-Video Generator") },
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
                Text("Enter Text Prompt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("A magical waterfall deep inside an enchanted crystal cave with glowing flora...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select Mode", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
                        val isSelected = duration == d
                        FilterChip(
                            selected = isSelected,
                            onClick = { duration = d },
                            label = { Text("${d}s") }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OptSelector(items = styles, selectedItem = style) { style = it }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val p = prompt.ifBlank { "Magical waterfall deep inside an enchanted crystal cave." }
                    onGenerate(
                        VideoGenerationRequest(
                            prompt = p,
                            mode = mode,
                            duration = duration,
                            aspectRatio = if (mode == "SHORT") "9:16" else "16:9",
                            style = style
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GENERATE AI VIDEO (${duration}s)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
