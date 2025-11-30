package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.domain.models.ChatMessage
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
    suspend fun saveChatMessage(
        operatorId: Long,
        userMessage: String,
        aiMessage: String,
        timestamp: Long
    ): Long
    suspend fun saveChatMessages(
        operatorId: Long,
        userMessage: ChatMessage,
        aiMessage: ChatMessage
    ): Long
    suspend fun clearChatHistory(operatorId: Long)
    fun getRecentChatHistory(operatorId: Long, limit: Int): Flow<List<ChatEntity>>
}