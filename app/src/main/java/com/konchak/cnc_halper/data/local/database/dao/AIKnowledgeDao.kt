// 📁 data/local/database/dao/AIKnowledgeDao.kt
package com.konchak.cnc_halper.data.local.database.dao

import androidx.room.*
import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIKnowledgeDao {

    // 1. Добавить новое знание
    @Insert
    suspend fun insert(knowledge: AIKnowledgeEntity)

    // 2. Найти похожие вопросы (поиск по ключевым словам)
    @Query("SELECT * FROM ai_knowledge WHERE question LIKE '%' || :keyword || '%'")
    suspend fun findSimilar(keyword: String): List<AIKnowledgeEntity>

    // 3. Получить все знания (для отладки)
    @Query("SELECT * FROM ai_knowledge ORDER BY createdAt DESC")
    fun getAll(): Flow<List<AIKnowledgeEntity>>

    // 4. Получить по категории
    @Query("SELECT * FROM ai_knowledge WHERE category = :category")
    suspend fun getByCategory(category: String): List<AIKnowledgeEntity>

    // 5. Увеличить счетчик использования
    @Query("UPDATE ai_knowledge SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsage(id: String)

    // 6. Пометить как проверенное
    @Query("UPDATE ai_knowledge SET verified = :verified WHERE id = :id")
    suspend fun setVerified(id: String, verified: Boolean = true)

    // 7. Получить самые популярные ответы
    @Query("SELECT * FROM ai_knowledge ORDER BY usageCount DESC LIMIT :limit")
    suspend fun getMostUsed(limit: Int = 10): List<AIKnowledgeEntity>

    // 8. Поиск по нескольким ключевым словам
    @Query("""
        SELECT * FROM ai_knowledge 
        WHERE question LIKE '%' || :keyword1 || '%' 
           OR question LIKE '%' || :keyword2 || '%'
        ORDER BY confidence DESC
    """)
    suspend fun searchByKeywords(keyword1: String, keyword2: String): List<AIKnowledgeEntity>

    // 9. Удалить (если оператор поправил)
    @Delete
    suspend fun delete(knowledge: AIKnowledgeEntity)

    // 10. Обновить confidence
    @Update
    suspend fun update(knowledge: AIKnowledgeEntity)
}