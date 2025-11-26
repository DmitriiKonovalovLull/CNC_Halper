package com.konchak.cnc_halper.presentation.main.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konchak.cnc_halper.domain.models.MessageType

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Ежедневный опрос оператора",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Статус опроса
        if (chatState.hasAnsweredToday) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "✅ Вы уже ответили на сегодняшние вопросы",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Список сообщений
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatState.messages) { message ->
                ChatBubble(
                    message = message.message,
                    isUser = message.isUser,
                    messageType = message.messageType
                )
            }
        }

        // Поле ввода
        if (!chatState.hasAnsweredToday) {
            MessageInput { message ->
                viewModel.sendMessage(message)
            }
        } else {
            Button(
                onClick = { viewModel.startDailyQuestions() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Пройти опрос еще раз")
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: String,
    isUser: Boolean,
    messageType: MessageType
) {
    val backgroundColor = when {
        isUser -> MaterialTheme.colorScheme.primaryContainer
        messageType == MessageType.DAILY_QUESTION -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isUser) {
            // Сообщение пользователя - справа
            Card(
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .widthIn(max = 280.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            // Сообщение системы - слева
            Card(
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .widthIn(max = 280.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MessageInput(onSendMessage: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Введите ваш ответ...") },
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onSendMessage(text)
                    text = ""
                }
            }
        ) {
            Text("Отправить")
        }
    }
}