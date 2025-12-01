package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.konchak.cnc_halper.domain.models.Tool

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolItem(
    tool: Tool,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (tool.imageUrl != null) {
                AsyncImage(
                    model = tool.imageUrl,
                    contentDescription = tool.name,
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(50.dp),
                    color = when (tool.wearLevel) {
                        1 -> Color(0xFF388E3C)
                        2 -> Color.Yellow
                        3 -> Color(0xFFFFA500)
                        4 -> Color.Red
                        else -> Color.Gray
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            tool.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    tool.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "${tool.type.displayName} • ${tool.getSizeString()}", // Use displayName
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Text(
                    "Износ: ${tool.getWearStatus()}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
    }
}
