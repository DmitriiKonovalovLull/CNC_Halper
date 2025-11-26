package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun hasOperatorAnsweredToday(operatorId: Long): Boolean
    suspend fun saveChatResponses(
        operatorId: Long,
        questions: List<String>,
        answers: List<String>
    ): Long
    fun getChatHistory(operatorId: Long): Flow<List<ChatEntity>>
    suspend fun getTodayResponses(operatorId: Long): ChatEntity?
    suspend fun syncData(): Boolean
}