package com.konchak.cnc_halper.presentation.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.ChatMessage
import com.konchak.cnc_halper.domain.models.MessageType
import com.konchak.cnc_halper.domain.repositories.AIRepository
import com.konchak.cnc_halper.domain.repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val currentOperatorId = 1L

    init {
        loadChatHistory()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> sendMessage(event.message)
            ChatEvent.ClearChatHistory -> clearChatHistory()
            ChatEvent.ClearError -> clearError()
            ChatEvent.RetryLoadHistory -> loadChatHistory()
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            _chatState.update { it.copy(isLoading = true, error = null) }

            val userMessage = ChatMessage(
                id = generateId(),
                message = message,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )

            _chatState.update { it.copy(messages = it.messages + userMessage) }

            val aiResponse = withContext(Dispatchers.IO) {
                try {
                    aiRepository.processWithHybridAI(message)
                } catch (e: Exception) {
                    AIResponse.Error("Ошибка: ${e.message ?: "Неизвестная ошибка"}")
                }
            }

            val aiMessage = when (aiResponse) {
                is AIResponse.Success -> ChatMessage(
                    id = generateId(),
                    message = aiResponse.answer,
                    isUser = false,
                    timestamp = System.currentTimeMillis(),
                    messageType = MessageType.TEXT
                )
                is AIResponse.Error -> ChatMessage(
                    id = generateId(),
                    message = aiResponse.message,
                    isUser = false,
                    timestamp = System.currentTimeMillis(),
                    messageType = MessageType.ERROR
                )
            }

            _chatState.update {
                it.copy(
                    messages = it.messages + aiMessage,
                    isLoading = false
                )
            }

            saveChatToHistory(userMessage, aiMessage)
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRepository.clearChatHistory(currentOperatorId)
                _chatState.update { it.copy(messages = emptyList()) }

                val systemMessage = ChatMessage(
                    id = generateId(),
                    message = "История чата очищена",
                    isUser = false,
                    timestamp = System.currentTimeMillis(),
                    messageType = MessageType.SYSTEM_MESSAGE
                )
                _chatState.update { it.copy(messages = it.messages + systemMessage) }

            } catch (e: Exception) {
                _chatState.update { it.copy(error = "Ошибка очистки: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _chatState.update { it.copy(error = null) }
    }

    private fun saveChatToHistory(userMessage: ChatMessage, aiMessage: ChatMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRepository.saveChatMessages(
                    operatorId = currentOperatorId,
                    userMessage = userMessage,
                    aiMessage = aiMessage
                )
            } catch (e: Exception) {
                println("Ошибка сохранения истории: ${e.message}")
            }
        }
    }

    private fun loadChatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRepository.getRecentChatHistory(currentOperatorId, 100).collect { chatEntities ->
                    val messages = chatEntities.flatMap { entity ->
                        listOf(
                            ChatMessage(
                                id = "q_${entity.id}",
                                message = entity.question1,
                                isUser = true,
                                timestamp = entity.date
                            ),
                            ChatMessage(
                                id = "a_${entity.id}",
                                message = entity.answer1,
                                isUser = false,
                                timestamp = entity.date
                            )
                        )
                    }
                    _chatState.update { it.copy(messages = messages, isLoading = false) }
                }
            } catch (e: Exception) {
                _chatState.update {
                    it.copy(
                        error = "Ошибка загрузки истории: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun generateId(): String = "msg_${System.currentTimeMillis()}_${(0..1000).random()}"
}

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ChatEvent {
    data class SendMessage(val message: String) : ChatEvent()
    object ClearChatHistory : ChatEvent()
    object ClearError : ChatEvent()
    object RetryLoadHistory : ChatEvent()
}