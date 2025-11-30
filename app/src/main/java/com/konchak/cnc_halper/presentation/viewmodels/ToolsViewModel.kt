package com.konchak.cnc_halper.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.data.models.Tool
import com.konchak.cnc_halper.domain.repository.ToolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToolsViewModel(
    private val repository: ToolRepository
) : ViewModel() {
    
    private val _tools = MutableStateFlow<List<Tool>>(emptyList())
    val tools: StateFlow<List<Tool>> = _tools.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse.asStateFlow()
    
    init {
        loadTools()
    }
    
    fun loadTools() {
        viewModelScope.launch {
            if (_searchQuery.value.isEmpty()) {
                repository.getAllTools().collect { toolsList ->
                    _tools.value = toolsList
                }
            } else {
                repository.searchTools(_searchQuery.value).collect { toolsList ->
                    _tools.value = toolsList
                }
            }
        }
    }
    
    fun searchTools(query: String) {
        _searchQuery.value = query
        loadTools()
    }
    
    fun addTool(tool: Tool) {
        viewModelScope.launch {
            repository.insertTool(tool)
            loadTools()
        }
    }
    
    fun updateTool(tool: Tool) {
        viewModelScope.launch {
            repository.updateTool(tool)
            loadTools()
        }
    }
    
    fun deleteTool(tool: Tool) {
        viewModelScope.launch {
            repository.deleteTool(tool)
            loadTools()
        }
    }
    
    fun askAI(question: String) {
        viewModelScope.launch {
            try {
                val response = repository.askAI(question)
                _aiResponse.value = response
            } catch (e: Exception) {
                _aiResponse.value = "Ошибка: ${e.message}"
            }
        }
    }
}
