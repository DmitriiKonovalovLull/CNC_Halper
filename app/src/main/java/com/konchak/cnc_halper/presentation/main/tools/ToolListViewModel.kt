package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolListViewModel @Inject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private val _tools = MutableStateFlow<List<Tool>>(emptyList())
    val tools: StateFlow<List<Tool>> = _tools

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        loadTools()
    }

    private fun loadTools() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                // ✅ ПОДПИСЫВАЕМСЯ НА FLOW ИЗ РЕПОЗИТОРИЯ
                toolRepository.getTools().collect { toolsList ->
                    println("🛠️ DEBUG: ToolListViewModel - получено инструментов: ${toolsList.size}")
                    _tools.value = toolsList
                }
            } catch (e: Exception) {
                println("🛠️ DEBUG: Ошибка загрузки инструментов: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}