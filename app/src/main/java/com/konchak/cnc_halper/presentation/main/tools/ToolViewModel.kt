// app/src/main/java/com/konchak/cnc_halper/presentation/main/tools/ToolViewModel.kt
package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _saveSuccess = MutableStateFlow(false)
    @Suppress("unused")
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    fun saveTool(
        operatorId: Long,
        name: String,
        type: String,
        size: String,
        photoPath: String?,
        onSuccess: () -> Unit
    ) {
        _isLoading.value = true
        _errorMessage.value = null
        _saveSuccess.value = false

        viewModelScope.launch {
            try {
                toolRepository.saveTool(operatorId, name, type, size, photoPath)
                _saveSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сохранения инструмента: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    @Suppress("unused")
    suspend fun getToolsCount(operatorId: Long): Int {
        return try {
            toolRepository.getToolsCount(operatorId)
        } catch (e: Exception) {
            _errorMessage.value = "Ошибка получения данных: ${e.message}"
            0
        }
    }
}
