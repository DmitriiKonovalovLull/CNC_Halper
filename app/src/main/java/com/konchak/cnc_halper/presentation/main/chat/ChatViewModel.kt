package com.konchak.cnc_halper.presentation.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.ChatMessage
import com.konchak.cnc_halper.domain.models.MessageType
import com.konchak.cnc_halper.domain.repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val currentOperatorId = 1L // ID текущего оператора
    private val dailyQuestions = listOf(
        "Какие станки сегодня в работе?",
        "Есть ли проблемы с инструментом?",
        "Нужна ли помощь с настройкой?"
    )

    private var currentQuestionIndex = 0
    private val userAnswers = mutableListOf<String>()

    init {
        checkDailyQuestions()
        loadChatHistory()
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            // Добавляем сообщение пользователя
            val userMessage = ChatMessage(
                id = generateId(),
                message = message,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )

            val currentMessages = _chatState.value.messages + userMessage
            _chatState.value = _chatState.value.copy(messages = currentMessages)

            // Сохраняем ответ пользователя
            userAnswers.add(message)

            // Проверяем, нужно ли задавать следующий вопрос
            if (currentQuestionIndex < dailyQuestions.size - 1) {
                currentQuestionIndex++
                showNextQuestion()
            } else {
                // Все вопросы отвечены - сохраняем в базу
                saveDailyResponses()
                _chatState.value = _chatState.value.copy(
                    hasAnsweredToday = true,
                    dailyQuestions = emptyList()
                )

                // Показываем благодарность
                showThankYouMessage()
            }
        }
    }

    fun startDailyQuestions() {
        currentQuestionIndex = 0
        userAnswers.clear()
        _chatState.value = _chatState.value.copy(
            dailyQuestions = dailyQuestions,
            hasAnsweredToday = false
        )
        showNextQuestion()
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex < dailyQuestions.size) {
            val questionMessage = ChatMessage(
                id = generateId(),
                message = dailyQuestions[currentQuestionIndex],
                isUser = false,
                timestamp = System.currentTimeMillis(),
                messageType = MessageType.DAILY_QUESTION
            )

            _chatState.value = _chatState.value.copy(
                messages = _chatState.value.messages + questionMessage
            )
        }
    }

    private fun showThankYouMessage() {
        val thankYouMessage = ChatMessage(
            id = generateId(),
            message = "Спасибо за ответы! Данные сохранены.",
            isUser = false,
            timestamp = System.currentTimeMillis(),
            messageType = MessageType.SYSTEM_MESSAGE
        )

        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages + thankYouMessage
        )
    }

    private suspend fun saveDailyResponses() {
        if (userAnswers.size == dailyQuestions.size) {
            chatRepository.saveChatResponses(
                operatorId = currentOperatorId,
                questions = dailyQuestions,
                answers = userAnswers
            )
        }
    }

    private fun checkDailyQuestions() {
        viewModelScope.launch {
            val hasAnswered = chatRepository.hasOperatorAnsweredToday(currentOperatorId)
            _chatState.value = _chatState.value.copy(hasAnsweredToday = hasAnswered)

            if (!hasAnswered) {
                // Автоматически начинаем ежедневные вопросы
                startDailyQuestions()
            }
        }
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            chatRepository.getChatHistory(currentOperatorId).collect { chatEntities ->
                val messages = chatEntities.flatMap { entity ->
                    listOf(
                        ChatMessage(
                            id = "q1_${entity.id}",
                            message = entity.question1,
                            isUser = false,
                            timestamp = entity.date,
                            messageType = MessageType.DAILY_QUESTION
                        ),
                        ChatMessage(
                            id = "a1_${entity.id}",
                            message = entity.answer1,
                            isUser = true,
                            timestamp = entity.date
                        ),
                        ChatMessage(
                            id = "q2_${entity.id}",
                            message = entity.question2,
                            isUser = false,
                            timestamp = entity.date,
                            messageType = MessageType.DAILY_QUESTION
                        ),
                        ChatMessage(
                            id = "a2_${entity.id}",
                            message = entity.answer2,
                            isUser = true,
                            timestamp = entity.date
                        ),
                        ChatMessage(
                            id = "q3_${entity.id}",
                            message = entity.question3,
                            isUser = false,
                            timestamp = entity.date,
                            messageType = MessageType.DAILY_QUESTION
                        ),
                        ChatMessage(
                            id = "a3_${entity.id}",
                            message = entity.answer3,
                            isUser = true,
                            timestamp = entity.date
                        )
                    )
                }
                _chatState.value = _chatState.value.copy(messages = messages)
            }
        }
    }

    private fun generateId(): String = "msg_${System.currentTimeMillis()}_${(0..1000).random()}"
}

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val dailyQuestions: List<String> = emptyList(),
    val hasAnsweredToday: Boolean = false,
    val isLoading: Boolean = false
)