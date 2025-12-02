package com.konchak.cnc_halper.presentation.main.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.presentation.main.tools.ScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanHistoryViewModel @Inject constructor(
    // Здесь будут инжектироваться репозитории для работы с историей сканирования
) : ViewModel() {

    private val _scanHistory = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanHistory: StateFlow<List<ScanResult>> = _scanHistory.asStateFlow()

    init {
        loadScanHistory()
    }

    private fun loadScanHistory() {
        viewModelScope.launch {
            // TODO: Загрузка истории сканирования из репозитория
            // Пока заглушка
            _scanHistory.value = listOf(
                ScanResult(quality = 95, imageSize = "10x50", toolId = "tool_1", imagePath = "path/to/image1.jpg"),
                ScanResult(quality = 88, imageSize = "12x60", toolId = "tool_2", imagePath = "path/to/image2.jpg"),
                ScanResult(quality = 70, imageSize = "8x40", toolId = "tool_3", imagePath = "path/to/image3.jpg")
            )
        }
    }
}
