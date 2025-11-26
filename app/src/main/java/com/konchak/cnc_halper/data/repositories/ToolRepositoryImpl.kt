// app/src/main/java/com/konchak/cnc_halper/data/repositories/ToolRepositoryImpl.kt
package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.ToolDao
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.data.local.database.mappers.ToolMapper
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolAnalysisResult
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
    private val toolDao: ToolDao
) : ToolRepository {

    override suspend fun saveTool(
        operatorId: Long,
        name: String,
        type: String,
        size: String,
        photoPath: String?
    ): Long {
        val tool = ToolEntity(
            operatorId = operatorId,
            name = name,
            type = type,
            size = size,
            photoPath = photoPath
        )
        return toolDao.insertTool(tool)
    }

    override fun getToolsByOperator(operatorId: Long): Flow<List<ToolEntity>> {
        return toolDao.getToolsByOperator(operatorId)
    }

    override suspend fun getToolsCount(operatorId: Long): Int {
        return toolDao.getToolsCount(operatorId)
    }

    override fun getAllTools(): Flow<List<ToolEntity>> {
        return toolDao.getAllTools()
    }

    override suspend fun deleteTool(id: Long) {
        toolDao.deleteTool(id)
    }

    override suspend fun syncData(): Boolean {
        // TODO: Implement actual sync logic
        return true
    }

    override suspend fun analyzeToolWear(toolId: String): ToolAnalysisResult {
        // TODO: Implement actual analysis logic
        return ToolAnalysisResult(
            toolId = toolId,
            wearLevel = 50,
            confidence = 0.8f,
            recommendations = listOf("Заточить инструмент", "Уменьшить скорость резания"),
            estimatedLife = 100,
            wearPercentage = 0f,
            remainingLife = 0
        )
    }

    override fun getTools(): Flow<List<Tool>> {
        return toolDao.getAllTools().map { entities ->
            entities.map { ToolMapper.toDomain(it) }
        }
    }

    override suspend fun scanTool(imagePath: String): Tool {
        // TODO: Implement actual scan logic
        return Tool(
            id = "1",
            name = "Фреза",
            type = "Концевая",
            diameter = 10f,
            length = 50f,
            material = "HSS",
            wearLevel = 0,
            lastUsed = System.currentTimeMillis(),
            machineId = "1",
            imagePath = imagePath,
            isActive = true,
            lastSync = System.currentTimeMillis(),
            createdAt = System.currentTimeMillis(),
            operatorId = 1L,
            size = "10x50",
            photoPath = imagePath
        )
    }
}