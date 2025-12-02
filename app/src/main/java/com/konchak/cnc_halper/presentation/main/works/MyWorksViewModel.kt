package com.konchak.cnc_halper.presentation.main.works

import androidx.lifecycle.ViewModel
import com.konchak.cnc_halper.domain.models.Work
import com.konchak.cnc_halper.domain.models.WorkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyWorksViewModel @Inject constructor() : ViewModel() {

    private val _works = MutableStateFlow<List<Work>>(emptyList())
    val works: StateFlow<List<Work>> = _works.asStateFlow()

    init {
        loadInitialWorks()
    }

    private fun loadInitialWorks() {
        _works.value = listOf(
            Work(
                id = "1",
                name = "Корпус редуктора",
                description = "Фрезеровка корпуса из Д16Т по чертежу КР-001.01. Требуется высокая точность.",
                startDate = System.currentTimeMillis() - 86400000 * 5, // 5 days ago
                status = WorkStatus.IN_PROGRESS,
                machineId = "machine_1",
                toolIds = listOf("tool_1", "tool_2", "tool_5")
            ),
            Work(
                id = "2",
                name = "Фланец по ГОСТ",
                description = "Изготовление фланца согласно ГОСТ 12820-80. Партия 50 шт.",
                startDate = System.currentTimeMillis() - 86400000 * 2, // 2 days ago
                isGostProject = true,
                gostStandards = listOf("ГОСТ 12820-80", "ГОСТ 12815-80"),
                status = WorkStatus.COMPLETED,
                endDate = System.currentTimeMillis() - 86400000 * 1, // yesterday
                machineId = "machine_2",
                toolIds = listOf("tool_3", "tool_4")
            ),
            Work(
                id = "3",
                name = "Вал приводной",
                description = "Токарная обработка вала из Сталь-45. Чистовая обработка.",
                startDate = System.currentTimeMillis() - 86400000 * 10, // 10 days ago
                status = WorkStatus.IN_PROGRESS,
                machineId = "machine_3",
                toolIds = listOf("tool_6", "tool_7")
            ),
            Work(
                id = "4",
                name = "Кронштейн крепления",
                description = "Сложная 5-осевая обработка кронштейна из титана.",
                startDate = System.currentTimeMillis() - 86400000 * 1, // yesterday
                status = WorkStatus.PLANNED
            ),
            Work(
                id = "5",
                name = "Плита монтажная",
                description = "Черновая и чистовая обработка плиты. Материал - чугун.",
                startDate = System.currentTimeMillis() - 86400000 * 15,
                status = WorkStatus.PAUSED
            )
        )
    }

    fun addWork(work: Work) {
        _works.value = _works.value + work
    }
}
