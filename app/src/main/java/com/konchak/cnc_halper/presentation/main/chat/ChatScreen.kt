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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.R
import com.konchak.cnc_halper.domain.models.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

enum class ChatTheme(
    val colors: ColorScheme,
    val backgroundRes: Int,
    val userMessageBrush: Brush,
    val aiMessageBrush: Brush
) {
    SCIENTIFIC(
        lightColorScheme(
            primary = Color(0xFF0D47A1),
            onPrimary = Color.White,
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002)
        ),
        R.drawable.scientific_pattern, // Используем векторный узор напрямую
        Brush.linearGradient(listOf(Color(0xFF42A5F5), Color(0xFF1976D2))),
        Brush.linearGradient(listOf(Color(0xFFFAFAFA), Color(0xFFE0E0E0)))
    ),
    FLOWERS(
        lightColorScheme(
            primary = Color(0xFF8E24AA),
            onPrimary = Color.White,
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002)
        ),
        R.drawable.flowers_pattern, // Используем векторный узор напрямую
        Brush.linearGradient(listOf(Color(0xFFCE93D8), Color(0xFFAB47BC))),
        Brush.linearGradient(listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0)))
    ),
    RAINBOW(
        lightColorScheme(
            primary = Color(0xFFD81B60),
            onPrimary = Color.White,
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002)
        ),
        R.drawable.rainbow_pattern, // Используем векторный узор напрямую
        Brush.linearGradient(listOf(Color(0xFFF06292), Color(0xFFEC407A))),
        Brush.linearGradient(listOf(Color(0xFFF8BBD0), Color(0xFFF48FB1)))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState.collectAsState()
    val lazyListState = rememberLazyListState()
    val selectedTheme by viewModel.theme.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }
    var showThemeMenu by remember { mutableStateOf(false) }

    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(chatState.messages.size - 1)
        }
    }

    MaterialTheme(colorScheme = selectedTheme.colors) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = selectedTheme.backgroundRes),
                    contentScale = ContentScale.Crop, // Растягиваем узор
                    alpha = 0.1f // Делаем его полупрозрачным
                )
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text("Чат с AI помощником")
                        },
                        actions = {
                            IconButton(onClick = { showClearDialog = true }) {
                                Icon(Icons.Default.Delete, "Очистить историю")
                            }
                            IconButton(onClick = { showThemeMenu = true }) {
                                Icon(Icons.Default.MoreVert, "Темы")
                            }
                            DropdownMenu(
                                expanded = showThemeMenu,
                                onDismissRequest = { showThemeMenu = false }
                            ) {
                                ChatTheme.entries.forEach { theme ->
                                    DropdownMenuItem(
                                        text = { Text(theme.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                        onClick = {
                                            viewModel.onEvent(ChatEvent.ChangeTheme(theme))
                                            showThemeMenu = false
                                        }
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
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
                        lazyListState = lazyListState,
                        theme = selectedTheme
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
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    theme: ChatTheme
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            ChatBubble(message = message, theme = theme)
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage, theme: ChatTheme) {
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

        MessageCard(message = message, isUser = isUser, theme = theme)

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            AvatarUser()
        }
    }
}

@Composable
private fun MessageCard(message: ChatMessage, isUser: Boolean, theme: ChatTheme) {
    Column(
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    brush = if (isUser) theme.userMessageBrush else theme.aiMessageBrush,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = message.message,
                modifier = Modifier.padding(12.dp),
                color = if (isUser) theme.colors.onPrimary else theme.colors.onSurface
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

    Surface(shadowElevation = 8.dp, color = Color.White.copy(alpha = 0.8f)) {
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
