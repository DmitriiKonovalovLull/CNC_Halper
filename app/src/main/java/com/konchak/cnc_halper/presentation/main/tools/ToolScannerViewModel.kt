// app/src/main/java/com/konchak/cnc_halper/presentation/main/tools/ToolScannerViewModel.kt
package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolType
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolScannerViewModel @Inject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ToolScannerState())
    val state: StateFlow<ToolScannerState> = _state

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun onEvent(event: ToolScannerEvent) {
        when (event) {
            ToolScannerEvent.RequestPermission -> requestPermission()
            ToolScannerEvent.ToggleFlash -> toggleFlash()
            ToolScannerEvent.CaptureImage -> captureImage()
            ToolScannerEvent.PickFromGallery -> pickFromGallery()
            ToolScannerEvent.AnalyzeTool -> analyzeTool()
            ToolScannerEvent.SaveTool -> saveScannedTool()
            ToolScannerEvent.OpenSettings -> openSettings()
        }
    }

    private fun requestPermission() {
        _state.value = _state.value.copy(
            hasCameraPermission = true
        )
    }

    private fun toggleFlash() {
        _state.value = _state.value.copy(
            isFlashOn = !_state.value.isFlashOn
        )
    }

    private fun captureImage() {
        _state.value = _state.value.copy(isAnalyzing = true)
        
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            
            _state.value = _state.value.copy(
                isAnalyzing = false,
                scanResult = ScanResult(
                    imagePath = "/temp/scanned_tool_${System.currentTimeMillis()}.jpg",
                    quality = 85,
                    imageSize = "2.3 MB",
                    toolId = null // Пока нет toolId
                )
            )
            
            println("🛠️ DEBUG: Сканирование завершено, scanResult создан")
        }
    }

    private fun pickFromGallery() {
        _state.value = _state.value.copy(
            scanResult = ScanResult(
                imagePath = "/gallery/tool_image.jpg", 
                quality = 90,
                imageSize = "1.8 MB",
                toolId = null // Пока нет toolId
            )
        )
    }

    private fun analyzeTool() {
        _state.value = _state.value.copy(isAnalyzing = true)
        
        viewModelScope.launch {
            kotlinx.coroutines.delay(1500)
            _state.value = _state.value.copy(isAnalyzing = false)
        }
    }

    private fun saveScannedTool() {
        println("🛠️ DEBUG: saveScannedTool() ВЫЗВАН")
        
        viewModelScope.launch {
            val currentScan = _state.value.scanResult
            println("🛠️ DEBUG: currentScan = $currentScan")
            
            if (currentScan != null) {
                println("🛠️ DEBUG: Создаем инструмент...")
                
                val newToolId = System.currentTimeMillis().toString()
                val tool = Tool(
                    id = newToolId,
                    name = "Сканированная фреза $newToolId",
                    type = ToolType.END_MILL,
                    diameter = 12.0f,
                    length = 50.0f,
                    material = "Твердый сплав",
                    wearLevel = 1,
                    status = "active",
                    createdAt = System.currentTimeMillis(),
                    operatorId = "1",
                    imageUrl = currentScan.imagePath
                )
                
                try {
                    println("🛠️ DEBUG: Вызываем toolRepository.addTool()")
                    toolRepository.addTool(tool)
                    println("🛠️ DEBUG: ✅ Инструмент сохранен успешно!")
                    
                    _state.value = _state.value.copy(
                        isSaved = true,
                        scanResult = currentScan.copy(toolId = newToolId) // Обновляем scanResult с toolId
                    )
                    _navigationEvent.value = "tool_saved"
                    
                } catch (e: Exception) {
                    println("🛠️ DEBUG: ❌ ОШИБКА при сохранении: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                println("🛠️ DEBUG: ❌ Нет данных для сохранения!")
            }
        }
    }

    private fun openSettings() {
        // TODO: Открыть настройки камеры
    }

    fun clearNavigation() {
        _navigationEvent.value = null
    }
}

data class ToolScannerState(
    val hasCameraPermission: Boolean = false,
    val shouldShowPermissionRationale: Boolean = false,
    val isFlashOn: Boolean = false,
    val isAnalyzing: Boolean = false,
    val scanResult: ScanResult? = null,
    val isSaved: Boolean = false
)

sealed class ToolScannerEvent {
    object RequestPermission : ToolScannerEvent()
    object ToggleFlash : ToolScannerEvent()
    object CaptureImage : ToolScannerEvent()
    object PickFromGallery : ToolScannerEvent()
    object AnalyzeTool : ToolScannerEvent()
    object SaveTool : ToolScannerEvent()
    object OpenSettings : ToolScannerEvent()
}

data class ScanResult(
    val imagePath: String,
    val quality: Int,
    val imageSize: String,
    val toolId: String? = null // Добавлено поле toolId
)