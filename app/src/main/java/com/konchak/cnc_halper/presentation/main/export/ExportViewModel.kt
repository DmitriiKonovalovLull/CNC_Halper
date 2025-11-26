// app/src/main/java/com/konchak/cnc_halper/presentation/main/export/ExportViewModel.kt
package com.konchak.cnc_halper.presentation.main.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val operatorRepository: OperatorRepository
) : ViewModel() {

    private val _operatorsCount = MutableStateFlow(0)
    val operatorsCount: StateFlow<Int> = _operatorsCount

    private val _chatResponsesCount = MutableStateFlow(0)
    val chatResponsesCount: StateFlow<Int> = _chatResponsesCount

    private val _toolsCount = MutableStateFlow(0)
    val toolsCount: StateFlow<Int> = _toolsCount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadDataCounts()
    }

    private fun loadDataCounts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _operatorsCount.value = operatorRepository.getOperatorCount()
                // Временно используем 0 для чата и инструментов
                _chatResponsesCount.value = 0
                _toolsCount.value = 0
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateCSVData(): String { // УБРАЛ operatorId параметр
        return buildString {
            // Заголовок CSV
            appendLine("Тип данных,Количество,Дата экспорта")

            // Данные операторов
            appendLine("Операторы,${_operatorsCount.value},${getCurrentDate()}")

            // Данные чата
            appendLine("Ответы чата,${_chatResponsesCount.value},${getCurrentDate()}")

            // Данные инструментов
            appendLine("Инструменты,${_toolsCount.value},${getCurrentDate()}")
        }
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}