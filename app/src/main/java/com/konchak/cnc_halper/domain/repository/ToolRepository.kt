package com.konchak.cnc_halper.domain.repository

import com.konchak.cnc_halper.data.models.Tool
import kotlinx.coroutines.flow.Flow

interface ToolRepository {
    fun getAllTools(): Flow<List<Tool>>
    fun searchTools(query: String): Flow<List<Tool>>
    suspend fun insertTool(tool: Tool)
    suspend fun updateTool(tool: Tool)
    suspend fun deleteTool(tool: Tool)
    suspend fun askAI(question: String): String
}
