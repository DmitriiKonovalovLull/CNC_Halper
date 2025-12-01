package com.konchak.cnc_halper.presentation.main.profile

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.konchak.cnc_halper.domain.models.EnergyMode
import com.konchak.cnc_halper.domain.models.SyncStatus
import com.konchak.cnc_halper.domain.repositories.AIRepository
import com.konchak.cnc_halper.domain.repositories.OperatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val operatorRepository: OperatorRepository,
    private val aiRepository: AIRepository,
    private val application: Application
) : ViewModel() {

    private val _operatorState = MutableStateFlow(OperatorState())
    val operatorState: StateFlow<OperatorState> = _operatorState

    private val _batteryLevel = MutableStateFlow(0)
    val batteryLevel: StateFlow<Int> = _batteryLevel

    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus

    val currentEnergyMode: EnergyMode = EnergyMode.STANDARD

    init {
        viewModelScope.launch {
            operatorRepository.getOperator().collect { operator ->
                _operatorState.value = _operatorState.value.copy(operator = operator)
            }
        }
        // TODO: Инициализация batteryLevel и syncStatus
    }

    fun manualSync() {
        // TODO: Implement manual sync
    }

    fun toggleBiometricAuth() {
        // TODO: Implement biometric auth toggle
    }

    fun exportKnowledge() {
        viewModelScope.launch {
            try {
                val knowledge = aiRepository.getAllKnowledge()
                val gson = Gson()
                val jsonString = gson.toJson(knowledge)

                val file = withContext(Dispatchers.IO) {
                    val file = File(application.cacheDir, "knowledge_export.json")
                    FileWriter(file).use { it.write(jsonString) }
                    file
                }

                val uri: Uri = FileProvider.getUriForFile(
                    application,
                    "${application.packageName}.fileprovider",
                    file
                )

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                application.startActivity(intent)

            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class OperatorState(
    val operator: com.konchak.cnc_halper.domain.models.Operator? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
