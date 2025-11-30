package com.konchak.cnc_halper.data.repository

import com.konchak.cnc_halper.BuildConfig
import com.konchak.cnc_halper.data.database.ToolDao
import com.konchak.cnc_halper.data.models.Tool
import com.konchak.cnc_halper.domain.repository.ToolRepository
import com.konchak.cnc_halper.network.DeepSeekApiService
import com.konchak.cnc_halper.network.models.DeepSeekRequest
import com.konchak.cnc_halper.network.models.Message
import kotlinx.coroutines.flow.Flow

class ToolRepositoryImpl(
    private val toolDao: ToolDao,
    private val apiService: DeepSeekApiService
) : ToolRepository {
    
    override fun getAllTools(): Flow<List<Tool>> = toolDao.getAll()
    
    override fun searchTools(query: String): Flow<List<Tool>> = toolDao.search(query)
    
    override suspend fun insertTool(tool: Tool) = toolDao.insert(tool)
    
    override suspend fun updateTool(tool: Tool) = toolDao.update(tool)
    
    override suspend fun deleteTool(tool: Tool) = toolDao.delete(tool)
    
    override suspend fun askAI(question: String): String {
        val request = DeepSeekRequest(
            messages = listOf(
                Message("system", "Ты помощник по CNC станкам. Помогай с инструментами, станками и режимами резания."),
                Message("user", question)
            )
        )
        
        val response = apiService.getChatCompletion(
            "Bearer ${BuildConfig.DEEPSEEK_API_KEY}",
            request
        )
        
        return response.choices.first().message.content
    }
}
