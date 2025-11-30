package com.konchak.cnc_halper.presentation.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Data class to represent the state of the offline chat screen
data class OfflineChatState(
    val modelInfo: MiniAIModel = MiniAIModel(),
    val capabilities: List<String> = emptyList(),
    val limitations: List<String> = emptyList(),
    val canSync: Boolean = true,
    val isSyncing: Boolean = false
)

// Sealed interface for events from the UI
sealed interface OfflineChatEvent {
    data object SyncModels : OfflineChatEvent
}

@HiltViewModel
class OfflineChatViewModel @Inject constructor(
    // Inject your use cases or repositories here.
    // For example:
    // private val getOfflineModelInfoUseCase: GetOfflineModelInfoUseCase,
    // private val syncAiModelsUseCase: SyncAiModelsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OfflineChatState())
    val state = _state.asStateFlow()

    init {
        loadOfflineData()
    }

    /**
     * Handles events sent from the UI.
     */
    fun onEvent(event: OfflineChatEvent) {
        when (event) {
            is OfflineChatEvent.SyncModels -> {
                syncModels()
            }
        }
    }

    /**
     * Loads initial information about the on-device model.
     */
    private fun loadOfflineData() {
        viewModelScope.launch {
            // In a real app, you would fetch this data from a repository or use case.
            // For demonstration, we're using placeholder data.
            _state.update {
                it.copy(
                    modelInfo = MiniAIModel(
                        version = "1.2.0-mini",
                        sizeBytes = 52428800L, // 50 MB
                        accuracy = 0.85f,
                        lastUpdated = System.currentTimeMillis() - (86400000 * 3) // 3 days ago
                    ),
                    capabilities = listOf(
                        "Basic G-Code & M-Code lookup",
                        "Simple calculations (e.g., feed rate)",
                        "Definitions of CNC terminology"
                    ),
                    limitations = listOf(
                        "Cannot access real-time information",
                        "Does not understand complex project context",
                        "Limited to pre-packaged knowledge base"
                    )
                )
            }
        }
    }

    /**
     * Simulates syncing the AI models with a remote server.
     */
    private fun syncModels() {
        viewModelScope.launch {
            _state.update { it.copy(isSyncing = true) }

            // Simulate a network delay
            kotlinx.coroutines.delay(2000)

            // Here you would call your use case to perform the actual sync.
            // On completion, update the state.
            _state.update {
                it.copy(
                    isSyncing = false,
                    modelInfo = it.modelInfo.copy(
                        version = "1.3.0-mini", // Example of updated data
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
