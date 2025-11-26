package com.konchak.cnc_halper.presentation.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.core.ai.MiniAIEngine
import com.konchak.cnc_halper.core.network.NetworkUtils
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineChatViewModel @Inject constructor(
    private val miniAIEngine: MiniAIEngine,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _state = MutableStateFlow(OfflineChatState())
    val state: StateFlow<OfflineChatState> = _state

    init {
        loadModelInfo()
        checkSyncCapability()
    }

    fun onEvent(event: OfflineChatEvent) {
        when (event) {
            OfflineChatEvent.SyncModels -> syncModels()
            OfflineChatEvent.RefreshInfo -> loadModelInfo()
        }
    }

    private fun loadModelInfo() {
        viewModelScope.launch {
            try {
                val modelInfo = miniAIEngine.getModelInfo()
                _state.update { state ->
                    state.copy(
                        modelInfo = modelInfo,
                        capabilities = getCapabilities(),
                        limitations = getLimitations()
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        error = "Ошибка загрузки информации о модели: ${e.message}"
                    )
                }
            }
        }
    }

    private fun checkSyncCapability() {
        _state.update { state ->
            state.copy(
                canSync = networkUtils.isConnected() && networkUtils.isWifiConnected()
            )
        }
    }

    private fun syncModels() {
        _state.update { it.copy(isSyncing = true) }

        viewModelScope.launch {
            try {
                // Имитация синхронизации моделей
                kotlinx.coroutines.delay(3000)

                _state.update { state ->
                    state.copy(
                        isSyncing = false,
                        modelInfo = state.modelInfo.copy(
                            version = "1.2.0",
                            accuracy = 0.88f,
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        isSyncing = false,
                        error = "Ошибка синхронизации: ${e.message}"
                    )
                }
            }
        }
    }

    private fun getCapabilities(): List<String> {
        return listOf(
            "Базовые рекомендации по режимам резания",
            "Анализ стандартных материалов (сталь, алюминий)",
            "Расчет параметров для常见 инструментов",
            "Определение степени износа инструмента",
            "Работа без интернет-соединения"
        )
    }

    private fun getLimitations(): List<String> {
        return listOf(
            "Точность на 10-15% ниже облачной версии",
            "Ограниченный набор материалов и инструментов",
            "Нет доступа к актуальным данным сообщества",
            "Не поддерживаются сложные расчеты и симуляции"
        )
    }
}

data class OfflineChatState(
    val modelInfo: MiniAIModel = MiniAIModel("", "", "", "", 0f, 0L, 0L),
    val capabilities: List<String> = emptyList(),
    val limitations: List<String> = emptyList(),
    val canSync: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null
)

sealed class OfflineChatEvent {
    object SyncModels : OfflineChatEvent()
    object RefreshInfo : OfflineChatEvent()
}