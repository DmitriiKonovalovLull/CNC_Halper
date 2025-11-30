package com.konchak.cnc_halper.presentation.main.machines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.domain.models.Operator // Импортируем доменную модель Operator
import com.konchak.cnc_halper.domain.models.UserRole
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperatorViewModel @Inject constructor(
    private val operatorRepository: OperatorRepository
) : ViewModel() {

    fun saveOperator(name: String, workshop: String, onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                val newOperator = Operator(
                    name = name,
                    workshop = workshop,
                    // Остальные поля будут иметь значения по умолчанию из доменной модели Operator
                    email = "", // Заглушка, так как email не передается здесь
                    shift = "day",
                    experience = 0,
                    role = UserRole.OPERATOR // Changed to use UserRole enum
                )
                val operatorId = operatorRepository.insertOperator(newOperator) // Changed saveOperator to insertOperator
                onSuccess(operatorId)
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Обработка ошибки
            }
        }
    }
}