package com.konchak.cnc_halper.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity
import com.konchak.cnc_halper.domain.repositories.AIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnowledgeViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _knowledgeList = MutableStateFlow<List<AIKnowledgeEntity>>(emptyList())
    val knowledgeList: StateFlow<List<AIKnowledgeEntity>> = _knowledgeList.asStateFlow()

    init {
        loadAllKnowledge()
    }

    private fun loadAllKnowledge() {
        viewModelScope.launch {
            _knowledgeList.value = aiRepository.getAllKnowledge()
        }
    }

    fun deleteKnowledge(knowledge: AIKnowledgeEntity) {
        viewModelScope.launch {
            aiRepository.deleteKnowledge(knowledge)
            loadAllKnowledge() // Refresh list
        }
    }
}
