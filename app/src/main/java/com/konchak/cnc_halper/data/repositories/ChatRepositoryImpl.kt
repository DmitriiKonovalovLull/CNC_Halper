// app/src/main/java/com/konchak/cnc_halper/data/repositories/ChatRepositoryImpl.kt
package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.ChatDao
import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
) : ChatRepository {

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
            question1 = questions[0],
            answer1 = answers[0],
            question2 = questions[1],
            answer2 = answers[1],
            question3 = questions[2],
            answer3 = answers[2]
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
}