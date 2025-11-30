package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat_responses WHERE operator_id = :operatorId AND date = :date")
    suspend fun getChatResponsesForDate(operatorId: Long, date: Long): ChatEntity?

    @Query("SELECT * FROM chat_responses WHERE operator_id = :operatorId ORDER BY date DESC")
    fun getChatHistory(operatorId: Long): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatResponse(chatResponse: ChatEntity): Long

    @Update
    suspend fun updateChatResponse(chatResponse: ChatEntity)

    @Query("DELETE FROM chat_responses WHERE id = :id")
    suspend fun deleteChatResponse(id: Long)

    @Query("SELECT COUNT(*) FROM chat_responses WHERE operator_id = :operatorId AND date = :date")
    suspend fun hasAnsweredToday(operatorId: Long, date: Long): Int

    // ИСПРАВЛЕННЫЕ методы - используем единый стиль с нижним подчеркиванием

    @Query("DELETE FROM chat_responses WHERE operator_id = :operatorId")
    suspend fun deleteChatHistory(operatorId: Long)

    @Query("SELECT * FROM chat_responses WHERE operator_id = :operatorId ORDER BY date DESC LIMIT :limit")
    fun getRecentChatHistory(operatorId: Long, limit: Int): Flow<List<ChatEntity>>
}