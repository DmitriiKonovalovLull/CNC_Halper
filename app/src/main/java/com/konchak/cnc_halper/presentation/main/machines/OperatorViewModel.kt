// app/src/main/java/com/konchak/cnc_halper/presentation/main/machines/OperatorViewModel.kt
package com.konchak.cnc_halper.presentation.main.machines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                val operatorId = operatorRepository.saveOperator(name, workshop)
                onSuccess(operatorId)
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Обработка ошибки
            }
        }
    }
}