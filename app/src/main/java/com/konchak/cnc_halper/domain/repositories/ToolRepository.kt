// app/src/main/java/com/konchak/cnc_halper/domain/repositories/ToolRepository.kt
package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolAnalysisResult
import kotlinx.coroutines.flow.Flow

interface ToolRepository {
    suspend fun saveTool(
        operatorId: Long,
        name: String,
        type: String,
        size: String,
        photoPath: String?
    ): Long

    fun getToolsByOperator(operatorId: Long): Flow<List<ToolEntity>>
    suspend fun getToolsCount(operatorId: Long): Int
    fun getAllTools(): Flow<List<ToolEntity>>
    suspend fun deleteTool(id: Long)
    suspend fun syncData(): Boolean
    suspend fun analyzeToolWear(toolId: String): ToolAnalysisResult
    fun getTools(): Flow<List<Tool>>
    suspend fun scanTool(imagePath: String): Tool
}