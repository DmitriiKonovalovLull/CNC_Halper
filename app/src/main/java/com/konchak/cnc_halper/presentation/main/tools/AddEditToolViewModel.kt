// app/src/main/java/com/konchak/cnc_halper/presentation/main/tools/AddEditToolViewModel.kt
package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.ManufacturerTool
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolType
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditToolViewModel @Inject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditToolState())
    val state: StateFlow<AddEditToolState> = _state

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    // ✅ ДЕБАУНС ДЛЯ ПОИСКА
    private var searchJob: Job? = null

    fun loadTool(toolId: String) {
        viewModelScope.launch {
            val tool = toolRepository.getToolById(toolId.toLongOrNull() ?: 0L)
            if (tool != null) {
                _state.value = _state.value.copy(
                    name = tool.name,
                    type = tool.type,
                    diameter = tool.diameter.toString(),
                    length = tool.length.toString(),
                    material = tool.material,
                    wearLevel = tool.wearLevel,
                    imageUrl = tool.imageUrl
                )
                validateForm()
            }
        }
    }

    fun onEvent(event: AddEditToolEvent) {
        when (event) {
            is AddEditToolEvent.NameChanged -> {
                _state.value = _state.value.copy(name = event.name)
                validateForm()
            }
            is AddEditToolEvent.TypeChanged -> {
                _state.value = _state.value.copy(type = event.type)
                validateForm()
            }
            is AddEditToolEvent.DiameterChanged -> {
                _state.value = _state.value.copy(diameter = event.diameter)
            }
            is AddEditToolEvent.LengthChanged -> {
                _state.value = _state.value.copy(length = event.length)
            }
            is AddEditToolEvent.MaterialChanged -> {
                _state.value = _state.value.copy(material = event.material)
            }
            is AddEditToolEvent.WearLevelChanged -> {
                _state.value = _state.value.copy(wearLevel = event.wearLevel)
            }
            is AddEditToolEvent.ImageUrlChanged -> {
                _state.value = _state.value.copy(imageUrl = event.imageUrl)
            }
            is AddEditToolEvent.SearchQueryChanged -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                performSearch(event.query)
            }
            is AddEditToolEvent.ManufacturerToolSelected -> {
                fillFormFromManufacturerTool(event.tool)
            }
            AddEditToolEvent.TakePhoto -> {
                // TODO: Камера
            }
            AddEditToolEvent.PickFromGallery -> {
                // TODO: Галерея
            }
        }
    }

    // ✅ РЕАЛЬНЫЙ ПОИСК ПО БАЗЕ ПРОИЗВОДИТЕЛЕЙ
    private fun performSearch(query: String) {
        searchJob?.cancel()
        
        searchJob = viewModelScope.launch {
            if (query.length >= 2) { // Поиск только при 2+ символах
                delay(300) // Дебаунс 300мс
                
                val results = toolRepository.searchTools(query)
                _state.value = _state.value.copy(searchResults = results)
            } else {
                _state.value = _state.value.copy(searchResults = emptyList())
            }
        }
    }

    // ✅ АВТОМАТИЧЕСКОЕ ЗАПОЛНЕНИЕ ФОРМЫ ИЗ ВЫБРАННОГО ИНСТРУМЕНТА
    private fun fillFormFromManufacturerTool(manufacturerTool: ManufacturerTool) {
        _state.value = _state.value.copy(
            name = manufacturerTool.name,
            type = ToolType.fromDisplayName(manufacturerTool.type), // Convert String to ToolType enum
            diameter = manufacturerTool.specifications.diameter.toString(),
            length = manufacturerTool.specifications.length.toString(),
            material = manufacturerTool.specifications.material,
            searchQuery = "", // Очищаем поиск
            searchResults = emptyList() // Скрываем результаты
        )
        validateForm()
    }

    fun saveTool() {
        viewModelScope.launch {
            try {
                val tool = Tool(
                    id = System.currentTimeMillis().toString(),
                    name = _state.value.name,
                    type = _state.value.type,
                    diameter = _state.value.diameter.toFloatOrNull() ?: 0f,
                    length = _state.value.length.toFloatOrNull() ?: 0f,
                    material = _state.value.material,
                    wearLevel = _state.value.wearLevel,
                    status = "active",
                    createdAt = System.currentTimeMillis(),
                    operatorId = "1"
                )
                
                toolRepository.addTool(tool)
                _navigationEvent.value = "tool_saved"
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTool(toolId: String) {
        viewModelScope.launch {
            try {
                val tool = toolRepository.getToolById(toolId.toLongOrNull() ?: 0L)
                if (tool != null) {
                    val updatedTool = tool.copy(
                        name = _state.value.name,
                        type = _state.value.type,
                        diameter = _state.value.diameter.toFloatOrNull() ?: 0f,
                        length = _state.value.length.toFloatOrNull() ?: 0f,
                        material = _state.value.material,
                        wearLevel = _state.value.wearLevel,
                        imageUrl = _state.value.imageUrl
                    )
                    toolRepository.updateTool(updatedTool)
                    _navigationEvent.value = "tool_updated"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validateForm() {
        val isValid = _state.value.name.isNotBlank() && _state.value.type != ToolType.OTHER
        _state.value = _state.value.copy(isFormValid = isValid)
    }

    fun clearNavigation() {
        _navigationEvent.value = null
    }
}

// ✅ ОБНОВЛЯЕМ СОСТОЯНИЕ ДЛЯ ПОИСКА
data class AddEditToolState(
    val name: String = "",
    val type: ToolType = ToolType.OTHER, // Changed to ToolType enum
    val diameter: String = "",
    val length: String = "",
    val material: String = "Твердый сплав",
    val wearLevel: Int = 1,
    val imageUrl: String? = null,
    val isFormValid: Boolean = false,
    val searchQuery: String = "", // ✅ ПОИСКОВЫЙ ЗАПРОС
    val searchResults: List<ManufacturerTool> = emptyList() // ✅ РЕЗУЛЬТАТЫ ПОИСКА
)

// ✅ ДОБАВЛЯЕМ СОБЫТИЯ ДЛЯ ПОИСКА
sealed class AddEditToolEvent {
    data class NameChanged(val name: String) : AddEditToolEvent()
    data class TypeChanged(val type: ToolType) : AddEditToolEvent() // Changed to ToolType enum
    data class DiameterChanged(val diameter: String) : AddEditToolEvent()
    data class LengthChanged(val length: String) : AddEditToolEvent()
    data class MaterialChanged(val material: String) : AddEditToolEvent()
    data class WearLevelChanged(val wearLevel: Int) : AddEditToolEvent()
    data class ImageUrlChanged(val imageUrl: String) : AddEditToolEvent()
    object TakePhoto : AddEditToolEvent()
    object PickFromGallery : AddEditToolEvent()
    data class SearchQueryChanged(val query: String) : AddEditToolEvent()
    data class ManufacturerToolSelected(val tool: ManufacturerTool) : AddEditToolEvent()
}
