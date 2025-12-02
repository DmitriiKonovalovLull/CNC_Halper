package com.konchak.cnc_halper.presentation.main.works

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Work
import com.konchak.cnc_halper.domain.repositories.WorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkDetailViewModel @Inject constructor(
    private val workRepository: WorkRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _work = MutableStateFlow<Work?>(null)
    val work: StateFlow<Work?> = _work.asStateFlow()

    private val workId: String = savedStateHandle.get<String>("workId")!!

    init {
        loadWork()
    }

    private fun loadWork() {
        viewModelScope.launch {
            // TODO: Загрузка работы из репозитория
            // Пока заглушка
            _work.value = Work(
                id = workId,
                name = "Работа $workId",
                description = "Описание работы",
                drawingUrl = "https://example.com/drawing.pdf",
                gostStandards = listOf("ГОСТ 2.109-73", "ГОСТ 2.307-2011"),
                operatorNotes = "Проверить допуски перед финишной обработкой.",
                toolIds = listOf("tool_1", "tool_2"),
                machineId = "machine_1"
            )
        }
    }
}
