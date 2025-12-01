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
import kotlin.math.PI

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val _theme = MutableStateFlow(ChatTheme.SCIENTIFIC)
    val theme: StateFlow<ChatTheme> = _theme.asStateFlow()

    private val currentOperatorId = 1L

    private val cuttingSpeeds = mapOf(
        "сталь" to 120.0,
        "алюминий" to 300.0,
        "чугун" to 100.0,
        "нержавейка" to 80.0,
        "титан" to 60.0
    )

    init {
        loadChatHistory()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> sendMessage(event.message)
            ChatEvent.ClearChatHistory -> clearChatHistory()
            ChatEvent.ClearError -> clearError()
            ChatEvent.RetryLoadHistory -> loadChatHistory()
            is ChatEvent.ChangeTheme -> _theme.value = event.theme
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

            val calculationResult = handleCalculation(message)
            if (calculationResult != null) {
                val aiMessage = ChatMessage(
                    id = generateId(),
                    message = calculationResult,
                    isUser = false,
                    timestamp = System.currentTimeMillis(),
                    messageType = MessageType.AI_RESPONSE
                )
                _chatState.update {
                    it.copy(
                        messages = it.messages + aiMessage,
                        isLoading = false
                    )
                }
                saveChatToHistory(userMessage, aiMessage)
                return@launch
            }

            val aiResponse = withContext(Dispatchers.IO) {
                try {
                    val knows = aiRepository.knowsAnswer(message)
                    if (knows) {
                        aiRepository.processWithHybridAI(message)
                    } else {
                        AIResponse.Success(
                            answer = "Я пока не знаю ответ. Вы можете научить меня в разделе 'Управление знаниями' в профиле.",
                            confidence = 0.1f,
                            source = "need_training"
                        )
                    }
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
                    messageType = if (aiResponse.source == "need_training") MessageType.NEED_TRAINING else MessageType.AI_RESPONSE
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

    private fun handleCalculation(message: String): String? {
        val lowerCaseMessage = message.lowercase()

        if ("обороты" in lowerCaseMessage || "рассчитай обороты" in lowerCaseMessage) {
            val diameter = findDiameter(lowerCaseMessage)
            val material = findMaterial(lowerCaseMessage)

            if (diameter == null) {
                return "Не могу найти диаметр инструмента в вашем запросе. Пожалуйста, укажите диаметр в мм (например, 'фреза 10мм')."
            }
            if (material == null) {
                return "Не могу найти материал. Пожалуйста, укажите материал (например, 'по алюминию'). Я знаю: ${cuttingSpeeds.keys.joinToString(", ")}."
            }

            val cuttingSpeed = cuttingSpeeds[material]
            if (cuttingSpeed == null) {
                return "Я не знаю рекомендуемую скорость резания для материала '$material'. Я знаю: ${cuttingSpeeds.keys.joinToString(", ")}."
            }

            val rpm = calculateRpm(diameter, cuttingSpeed)
            return "Для диаметра $diameter мм и материала '$material' (V_c = $cuttingSpeed м/мин), рекомендуемые обороты: ${rpm.toInt()} об/мин."
        }

        if ("подача" in lowerCaseMessage) {
            val rpm = "обороты\\s*(\\d+\\.?\\d*)".toRegex().find(lowerCaseMessage)?.groupValues?.get(1)?.toDoubleOrNull()
            val feedPerTooth = "подача на зуб\\s*(\\d+\\.?\\d*)".toRegex().find(lowerCaseMessage)?.groupValues?.get(1)?.toDoubleOrNull()
            val teeth = "количество зубьев\\s*(\\d+)".toRegex().find(lowerCaseMessage)?.groupValues?.get(1)?.toIntOrNull()
            if (rpm != null && feedPerTooth != null && teeth != null) {
                val feedRate = calculateFeedRate(rpm, feedPerTooth, teeth)
                return "Подача: ${feedRate.toInt()} мм/мин"
            } else {
                return "Для расчета подачи укажите обороты, подачу на зуб и количество зубьев."
            }
        }

        if ("скорость резания" in lowerCaseMessage) {
            val diameter = findDiameter(lowerCaseMessage)
            val rpm = "обороты\\s*(\\d+\\.?\\d*)".toRegex().find(lowerCaseMessage)?.groupValues?.get(1)?.toDoubleOrNull()
            if (diameter != null && rpm != null) {
                val speed = calculateCuttingSpeed(diameter, rpm)
                return "Скорость резания: ${"%.2f".format(speed)} м/мин"
            } else {
                return "Для расчета скорости резания укажите диаметр и обороты."
            }
        }

        return null
    }

    private fun findDiameter(text: String): Double? {
        val regexPatterns = listOf(
            """(\d+\.?\d*)\s*мм""".toRegex(),
            """диаметр\s*(\d+\.?\d*)""".toRegex(),
            """(фрез[аы]|сверл[ао]|метчик[аи]?)\s*(\d+\.?\d*)""".toRegex()
        )

        for (pattern in regexPatterns) {
            val match = pattern.find(text)
            if (match != null) {
                val groupIndex = if (pattern.pattern.contains("фрез")) 2 else 1
                return match.groupValues.getOrNull(groupIndex)?.toDoubleOrNull()
            }
        }
        return null
    }

    private fun findMaterial(text: String): String? {
        return cuttingSpeeds.keys.find { "по $it" in text || "для $it" in text || it in text }
    }

    private fun calculateRpm(diameter: Double, cuttingSpeed: Double): Double {
        if (diameter == 0.0) return 0.0
        return (cuttingSpeed * 1000) / (PI * diameter)
    }

    private fun calculateCuttingSpeed(diameter: Double, rpm: Double): Double {
        return (diameter * PI * rpm) / 1000
    }

    private fun calculateFeedRate(rpm: Double, feedPerTooth: Double, teeth: Int): Double {
        return rpm * feedPerTooth * teeth
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
    data class ChangeTheme(val theme: ChatTheme) : ChatEvent()
    object ClearChatHistory : ChatEvent()
    object ClearError : ChatEvent()
    object RetryLoadHistory : ChatEvent()
}
