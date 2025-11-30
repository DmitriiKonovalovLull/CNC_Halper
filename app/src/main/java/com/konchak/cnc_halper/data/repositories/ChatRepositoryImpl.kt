package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.ChatDao
import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.domain.models.ChatMessage
import com.konchak.cnc_halper.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
) : ChatRepository {

    // Существующие методы
    override suspend fun hasOperatorAnsweredToday(operatorId: Long): Boolean {
        val today = System.currentTimeMillis()
        return chatDao.hasAnsweredToday(operatorId, today) > 0
    }

    override suspend fun saveChatResponses(
        operatorId: Long,
        questions: List<String>,
        answers: List<String>
    ): Long {
        val today = System.currentTimeMillis()

        val chatResponse = ChatEntity(
            operatorId = operatorId,
            date = today,
            question1 = questions.getOrNull(0) ?: "",
            answer1 = answers.getOrNull(0) ?: "",
            question2 = questions.getOrNull(1) ?: "",
            answer2 = answers.getOrNull(1) ?: "",
            question3 = questions.getOrNull(2) ?: "",
            answer3 = answers.getOrNull(2) ?: ""
        )

        return chatDao.insertChatResponse(chatResponse)
    }

    override fun getChatHistory(operatorId: Long): Flow<List<ChatEntity>> {
        return chatDao.getChatHistory(operatorId)
    }

    override suspend fun getTodayResponses(operatorId: Long): ChatEntity? {
        val today = System.currentTimeMillis()
        return chatDao.getChatResponsesForDate(operatorId, today)
    }

    override suspend fun syncData(): Boolean {
        // TODO: Implement actual sync logic
        return true
    }

    // НОВЫЕ методы для обычного чата

    override suspend fun saveChatMessage(
        operatorId: Long,
        userMessage: String,
        aiMessage: String,
        timestamp: Long
    ): Long {
        val chatEntity = ChatEntity(
            operatorId = operatorId,
            date = timestamp,
            question1 = userMessage,
            answer1 = aiMessage,
            question2 = "",
            answer2 = "",
            question3 = "",
            answer3 = ""
        )

        return chatDao.insertChatResponse(chatEntity)
    }

    override suspend fun saveChatMessages(
        operatorId: Long,
        userMessage: ChatMessage,
        aiMessage: ChatMessage
    ): Long {
        return saveChatMessage(
            operatorId = operatorId,
            userMessage = userMessage.message,
            aiMessage = aiMessage.message,
            timestamp = userMessage.timestamp
        )
    }

    override suspend fun clearChatHistory(operatorId: Long) {
        chatDao.deleteChatHistory(operatorId)
    }

    override fun getRecentChatHistory(operatorId: Long, limit: Int): Flow<List<ChatEntity>> {
        return chatDao.getRecentChatHistory(operatorId, limit)
    }
}
