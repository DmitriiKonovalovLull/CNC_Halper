// app/src/main/java/com/konchak/cnc_halper/presentation/main/tools/ToolDetailViewModel.kt
package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolUsageRecord
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolDetailViewModel @Inject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private val _tool = MutableStateFlow<Tool?>(null)
    val tool: StateFlow<Tool?> = _tool

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun loadTool(toolId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val tool = toolRepository.getToolById(toolId.toLongOrNull() ?: 0L)
                _tool.value = tool

                if (tool == null) {
                    _errorMessage.value = "Инструмент не найден"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Не удалось загрузить инструмент: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ ИСПРАВЛЕНО: добавлен параметр toolId
    fun markToolUsed(toolId: String) {
        _navigationEvent.value = "end_operation/$toolId"
    }

    // ✅ ИСПРАВЛЕНО: добавлен параметр toolId
    fun markForReplacement(toolId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Загружаем актуальные данные инструмента
                val currentTool = toolRepository.getToolById(toolId.toLongOrNull() ?: 0L)
                if (currentTool != null) {
                    val updatedTool = currentTool.copy(
                        wearLevel = 4, // Критический износ
                        status = "needs_replacement"
                    )
                    toolRepository.updateTool(updatedTool)
                    _tool.value = updatedTool // Обновляем UI
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteTool(toolId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                toolRepository.deleteTool(toolId.toLongOrNull() ?: 0L)
                _navigationEvent.value = "tool_deleted"
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления: ${e.message}"
            }
        }
    }

    // В ToolDetailViewModel.kt - исправленный метод
    fun addUsageRecord(toolId: String, wearLevel: Int, notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentTool = toolRepository.getToolById(toolId.toLongOrNull() ?: 0L)
                if (currentTool != null) {
                    val newRecord = ToolUsageRecord(
                        timestamp = System.currentTimeMillis(),
                        duration = System.currentTimeMillis() - currentTool.lastUsed,
                        machineId = currentTool.machineId ?: "unknown",
                        finalWearLevel = wearLevel,
                        notes = notes
                    )
                    val updatedTool = currentTool.copy(
                        usageHistory = currentTool.usageHistory + newRecord,
                        wearLevel = wearLevel,
                        lastUsed = System.currentTimeMillis()
                    )
                    toolRepository.updateTool(updatedTool)
                    _tool.value = updatedTool
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }
    fun clearNavigation() {
        _navigationEvent.value = null
    }
}