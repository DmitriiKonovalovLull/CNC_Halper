package com.konchak.cnc_halper.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.core.utils.getCurrentShiftBasedOnDate // Import the utility function
import com.konchak.cnc_halper.domain.models.Operator
import com.konchak.cnc_halper.domain.models.UserRole
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID // Import UUID for generating new IDs
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val operatorRepository: OperatorRepository
) : ViewModel() {

    private val _operator = MutableStateFlow<Operator?>(null)
    val operator: StateFlow<Operator?> = _operator.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _role = MutableStateFlow<UserRole?>(null)
    val role: StateFlow<UserRole?> = _role.asStateFlow()

    private val _workshop = MutableStateFlow("")
    val workshop: StateFlow<String> = _workshop.asStateFlow()

    private val _shift = MutableStateFlow("")
    val shift: StateFlow<String> = _shift.asStateFlow()

    private val _experience = MutableStateFlow("")
    val experience: StateFlow<String> = _experience.asStateFlow()

    init {
        viewModelScope.launch {
            operatorRepository.getOperator().collect { currentOperator ->
                _operator.value = currentOperator
                currentOperator?.let {
                    _name.value = it.name
                    _role.value = it.role
                    _workshop.value = it.workshop
                    _experience.value = it.experience.toString()
                    // Use saved shift if available, otherwise use date-based shift
                    _shift.value = if (it.shift.isNotBlank()) it.shift else getCurrentShiftBasedOnDate()
                } ?: run {
                    // If no operator exists, set default shift based on date
                    _shift.value = getCurrentShiftBasedOnDate()
                }
            }
        }
    }

    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onRoleChanged(newRole: UserRole) {
        _role.value = newRole
    }

    fun onWorkshopChanged(newWorkshop: String) {
        _workshop.value = newWorkshop
    }

    fun onShiftChanged(newShift: String) {
        _shift.value = newShift
    }

    fun onExperienceChanged(newExperience: String) {
        _experience.value = newExperience
    }

    fun saveProfile() {
        viewModelScope.launch {
            val currentOperator = _operator.value
            val experienceInt = _experience.value.toIntOrNull() ?: 0

            if (currentOperator == null) {
                // No operator exists, create a new one
                val newOperator = Operator(
                    id = UUID.randomUUID().toString(), // Generate a new ID
                    name = _name.value,
                    role = _role.value ?: UserRole.OPERATOR, // Default to OPERATOR if null
                    workshop = _workshop.value,
                    shift = _shift.value, // Use the current value from the UI
                    experience = experienceInt
                )
                operatorRepository.insertOperator(newOperator)
                _operator.value = newOperator // Update the ViewModel's operator state
            } else {
                // Operator exists, update it
                val updatedOperator = currentOperator.copy(
                    name = _name.value,
                    role = _role.value ?: UserRole.OPERATOR, // Default to OPERATOR if null
                    workshop = _workshop.value,
                    shift = _shift.value, // Use the current value from the UI
                    experience = experienceInt
                )
                operatorRepository.updateOperator(updatedOperator)
            }
        }
    }
}
