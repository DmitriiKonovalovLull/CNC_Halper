package com.konchak.cnc_halper.presentation.main.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
@Composable
fun OfflineChatMode(
    viewModel: OfflineChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Offline Mode Header
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📶", // Emoji instead of WifiOff icon
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Offline Mode",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Using on-device Mini-AI",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Model Information
        ModelInfoCard(
            modelInfo = state.modelInfo,
            modifier = Modifier.fillMaxWidth()
        )

        // Mini-AI Capabilities
        CapabilitiesList(
            capabilities = state.capabilities,
            modifier = Modifier.fillMaxWidth()
        )

        // Limitations
        LimitationsCard(
            limitations = state.limitations,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        // Sync Button
        if (state.canSync) {
            Button(
                onClick = { viewModel.onEvent(OfflineChatEvent.SyncModels) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSyncing
            ) {
                if (state.isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sync AI Models")
                }
            }
        }
    }
}

@Composable
fun ModelInfoCard(
    modelInfo: MiniAIModel,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🤖 Mini-AI Model", // Emoji for the title
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Version:", style = MaterialTheme.typography.bodyMedium)
                Text(modelInfo.version, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Size:", style = MaterialTheme.typography.bodyMedium)
                Text(String.format(Locale.getDefault(), "%.2f MB", modelInfo.sizeBytes / (1024f * 1024f)), style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Accuracy:", style = MaterialTheme.typography.bodyMedium)
                Text(String.format(Locale.getDefault(), "%.2f%%", modelInfo.accuracy * 100), style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Updated:", style = MaterialTheme.typography.bodyMedium)
                Text(SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(modelInfo.lastUpdated)), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CapabilitiesList(
    capabilities: List<String>,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "✅ Mini-AI Capabilities", // Emoji for the title
                style = MaterialTheme.typography.titleMedium
            )

            capabilities.forEach { capability ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓", // Symbol instead of an icon
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = capability,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun LimitationsCard(
    limitations: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "⚠️ Offline Mode Limitations", // Emoji for the title
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            limitations.forEach { limitation ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "•", // Bullet point instead of an icon
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = limitation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
