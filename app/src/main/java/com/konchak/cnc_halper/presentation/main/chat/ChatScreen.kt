package com.konchak.cnc_halper.presentation.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.domain.models.ChatMessage
import com.konchak.cnc_halper.domain.models.MessageType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState.collectAsState()
    val lazyListState = rememberLazyListState()

    var showClearDialog by remember { mutableStateOf(false) }

    // Automatically scroll to the bottom when a new message is added
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Чат с AI помощником")
                },
                actions = {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.Delete, "Очистить историю")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MessagesList(
                messages = chatState.messages,
                modifier = Modifier.weight(1f),
                lazyListState = lazyListState
            )

            StatusPanel(
                isLoading = chatState.isLoading,
                error = chatState.error,
                onRetry = { viewModel.onEvent(ChatEvent.RetryLoadHistory) },
                onClearError = { viewModel.onEvent(ChatEvent.ClearError) }
            )

            MessageInput(
                onSendMessage = { message ->
                    viewModel.onEvent(ChatEvent.SendMessage(message))
                }
            )
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Очистить историю") },
            text = { Text("Вы уверены, что хотите очистить всю историю чата?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(ChatEvent.ClearChatHistory)
                        showClearDialog = false
                    }
                ) {
                    Text("Очистить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
private fun MessagesList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier,
    lazyListState: androidx.compose.foundation.lazy.LazyListState
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        reverseLayout = true,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(messages.reversed(), key = { it.id }) { message ->
            ChatBubble(message = message)
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            AvatarAI()
            Spacer(modifier = Modifier.width(8.dp))
        }

        MessageCard(message = message, isUser = isUser)

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            AvatarUser()
        }
    }
}

@Composable
private fun MessageCard(message: ChatMessage, isUser: Boolean) {
    Column(
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isUser -> MaterialTheme.colorScheme.primary
                    message.messageType == MessageType.ERROR -> MaterialTheme.colorScheme.errorContainer
                    message.messageType == MessageType.SYSTEM_MESSAGE -> MaterialTheme.colorScheme.surfaceVariant
                    else -> MaterialTheme.colorScheme.surface
                }
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.message,
                modifier = Modifier.padding(12.dp),
                color = when {
                    isUser -> MaterialTheme.colorScheme.onPrimary
                    message.messageType == MessageType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
        Text(
            text = formatMessageTime(message.timestamp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
        )
    }
}

@Composable
private fun AvatarAI() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "AI",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun AvatarUser() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Я",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun MessageInput(onSendMessage: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val isSendEnabled = text.isNotBlank()

    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение...") },
                singleLine = false,
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    onSendMessage(text.trim())
                    text = ""
                },
                enabled = isSendEnabled
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, "Отправить")
            }
        }
    }
}

@Composable
private fun StatusPanel(
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onClearError: () -> Unit
) {
    if (isLoading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("AI думает...", style = MaterialTheme.typography.bodySmall)
        }
    }

    if (error != null) {
        AlertDialog(
            onDismissRequest = onClearError,
            title = { Text("Ошибка") },
            text = { Text(error) },
            confirmButton = {
                Button(onClick = onRetry) {
                    Text("Повторить")
                }
            },
            dismissButton = {
                TextButton(onClick = onClearError) {
                    Text("Закрыть")
                }
            }
        )
    }
}

private fun formatMessageTime(timestamp: Long): String {
    return try {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
    } catch (e: Exception) {
        "??:??"
    }
}
