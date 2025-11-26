package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ToolScannerViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ToolScannerState())
    val state: StateFlow<ToolScannerState> = _state

    fun onEvent(event: ToolScannerEvent) {
        when (event) {
            // TODO: Implement event handling
            else -> {}
        }
    }
}

data class ToolScannerState(
    val isFlashOn: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val shouldShowPermissionRationale: Boolean = false,
    val isAnalyzing: Boolean = false,
    val scanResult: ScanResult? = null
)

data class ScanResult(
    val quality: Int,
    val imageSize: String
)

sealed class ToolScannerEvent {
    object ToggleFlash : ToolScannerEvent()
    object RequestPermission : ToolScannerEvent()
    object PickFromGallery : ToolScannerEvent()
    object CaptureImage : ToolScannerEvent()
    object OpenSettings : ToolScannerEvent()
    object AnalyzeTool : ToolScannerEvent()
    object SaveTool : ToolScannerEvent()
}