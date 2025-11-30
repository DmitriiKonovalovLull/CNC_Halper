package com.konchak.cnc_halper.presentation.main.works

import androidx.lifecycle.ViewModel
import com.konchak.cnc_halper.domain.models.Work
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyWorksViewModel @Inject constructor() : ViewModel() {

    private val _works = MutableStateFlow<List<Work>>(emptyList())
    val works: StateFlow<List<Work>> = _works.asStateFlow()

    init {
        // Add some dummy data for demonstration
        _works.value = listOf(
            Work(
                id = "1",
                name = "Деталь 1",
                description = "Обработка детали по чертежу АБВГ.123456.001",
                startDate = System.currentTimeMillis() - 86400000 * 5, // 5 days ago
                isGostProject = false
            ),
            Work(
                id = "2",
                name = "Корпус ГОСТ",
                description = "Изготовление корпуса согласно ГОСТ 2.104-2006",
                startDate = System.currentTimeMillis() - 86400000 * 2, // 2 days ago
                isGostProject = true,
                gostType = "ГОСТ 2.104-2006"
            ),
            Work(
                id = "3",
                name = "Вал",
                description = "Токарная обработка вала",
                startDate = System.currentTimeMillis() - 86400000 * 10, // 10 days ago
                isGostProject = false
            )
        )
    }

    fun addWork(work: Work) {
        _works.value = _works.value + work
    }

    // You might add functions for updating, deleting, or fetching works from a repository here
}
