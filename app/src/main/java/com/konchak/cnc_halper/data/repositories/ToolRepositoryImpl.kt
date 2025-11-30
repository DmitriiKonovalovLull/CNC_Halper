// app/src/main/java/com/konchak/cnc_halper/data/repositories/ToolRepositoryImpl.kt
package com.konchak.cnc_halper.data.repositories

import com.konchak.cnc_halper.data.local.database.dao.ToolDao
import com.konchak.cnc_halper.data.local.database.entities.ToolEntity
import com.konchak.cnc_halper.data.local.database.mappers.ToolMapper
import com.konchak.cnc_halper.domain.models.CuttingParameters
import com.konchak.cnc_halper.domain.models.ManufacturerTool
import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ToolAnalysisResult
import com.konchak.cnc_halper.domain.models.ToolSpecifications
import com.konchak.cnc_halper.domain.models.ToolType
import com.konchak.cnc_halper.domain.repositories.ToolRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
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
        val toolEntity = ToolEntity(
            operatorId = operatorId,
            name = name,
            type = type,
            size = size,
            photoPath = photoPath,
            isSynced = false
        )
        return toolDao.insertTool(toolEntity)
    }

    override fun getToolsByOperator(operatorId: Long): Flow<List<Tool>> {
        return toolDao.getToolsByOperator(operatorId).map { entities ->
            entities.map { ToolMapper.toDomain(it) }
        }
    }

    override suspend fun getToolsCount(operatorId: Long): Int {
        return toolDao.getToolsCount(operatorId)
    }

    override fun getAllTools(): Flow<List<Tool>> {
        return toolDao.getAllTools().map { entities ->
            entities.map { ToolMapper.toDomain(it) }
        }
    }

    override suspend fun deleteTool(id: Long) {
        toolDao.deleteTool(id)
    }

    override suspend fun syncData(): Boolean {
        // Временная заглушка
        return true
    }

    override suspend fun analyzeToolWear(toolId: String): ToolAnalysisResult {
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

    override suspend fun scanTool(imagePath: String): Tool {
        // Временная заглушка
        val scannedTool = Tool(
            id = "scanned_${System.currentTimeMillis()}",
            name = "Сканированная фреза",
            type = ToolType.END_MILL, // Changed to ToolType enum
            diameter = 10f,
            length = 50f,
            material = "HSS",
            wearLevel = 2,
            lastUsed = System.currentTimeMillis(),
            machineId = "scanner",
            imageUrl = imagePath,
            status = "active",
            createdAt = System.currentTimeMillis(),
            operatorId = "1",
        )

        // Сохраняем сканированный инструмент
        saveTool(
            operatorId = scannedTool.operatorId.toLong(),
            name = scannedTool.name,
            type = scannedTool.type.displayName, // Changed to displayName
            size = "${scannedTool.diameter}x${scannedTool.length}",
            photoPath = scannedTool.imageUrl
        )

        return scannedTool
    }

    override suspend fun getCuttingRecommendations(tool: Tool, material: String): CuttingParameters {
        // TODO: Реализовать интеллектуальный подбор параметров

        // Временная реализация с базовой логикой
        return when (material.lowercase(Locale.ROOT)) {
            "сталь", "steel" -> CuttingParameters(
                cuttingSpeed = 125f,
                feedRate = 0.15f,
                depthOfCut = 1.5f,
                recommendations = listOf(
                    "Использовать СОЖ для охлаждения",
                    "Контролировать образование сливной стружки",
                    "Проверять заточку инструмента каждые 4 часа"
                )
            )
            "алюминий", "aluminum" -> CuttingParameters(
                cuttingSpeed = 300f,
                feedRate = 0.3f,
                depthOfCut = 2.0f,
                recommendations = listOf(
                    "Использовать острый инструмент",
                    "Избегать налипания материала",
                    "Обеспечить хороший отвод стружки"
                )
            )
            else -> CuttingParameters(
                cuttingSpeed = 150f,
                feedRate = 0.2f,
                depthOfCut = 1.5f,
                recommendations = listOf("Использовать стандартные параметры")
            )
        }
    }

    override fun getTools(): Flow<List<Tool>> {
        return toolDao.getAllTools().map { entities ->
            entities.map { ToolMapper.toDomain(it) }
        }
    }

    override suspend fun getToolById(id: Long): Tool? {
        return toolDao.getToolById(id)?.let { ToolMapper.toDomain(it) }
    }

    override suspend fun updateTool(tool: Tool) {
        val entity = ToolMapper.toEntity(tool)
        toolDao.updateTool(entity)
    }

    override suspend fun searchTools(query: String): List<ManufacturerTool> {
        delay(500) // Имитация сетевого запроса
        val lowerCaseQuery = query.lowercase(Locale.ROOT)
        return dummyManufacturerTools.filter {
            it.name.lowercase(Locale.ROOT).contains(lowerCaseQuery) ||
                    it.code.lowercase(Locale.ROOT).contains(lowerCaseQuery) ||
                    it.manufacturer.lowercase(Locale.ROOT).contains(lowerCaseQuery)
        }
    }

    override suspend fun getToolByCode(code: String): ManufacturerTool? {
        // TODO: Implement actual search logic
        return null
    }

    override suspend fun getSimilarTools(tool: Tool): List<ManufacturerTool> {
        // TODO: Implement actual search logic
        return emptyList()
    }

    override suspend fun uploadToolImage(imagePath: String): String? {
        // TODO: Implement actual upload logic
        return null
    }

    override suspend fun getUnsyncedTools(): List<Tool> {
        return toolDao.getUnsyncedTools().map { ToolMapper.toDomain(it) }
    }

    override suspend fun updateWearLevel(toolId: Long, wearLevel: Int) {
        // TODO: Implement actual update logic
    }

    override suspend fun markToolUsed(toolId: Long, machineId: String) {
        // TODO: Implement actual mark used logic
    }

    override suspend fun addTool(tool: Tool) {
        val entity = ToolMapper.toEntity(tool).copy(
            id = 0 // Room сам сгенерирует ID
        )
        toolDao.insertTool(entity)
    }

    override suspend fun deleteTool(tool: Tool) {
        // Используем правильный ID
        toolDao.deleteTool(tool.id.toLongOrNull() ?: 0L)
    }
}

// Временные данные для имитации базы производителей
val dummyManufacturerTools = listOf(
    ManufacturerTool(
        id = "SANDVIK_R390-11T308M-PM",
        name = "Фреза торцевая Sandvik CoroMill 390",
        code = "R390-11T308M-PM",
        type = "Торцевая фреза",
        manufacturer = "Sandvik Coromant",
        country = "Швеция",
        specifications = ToolSpecifications(
            diameter = 10f,
            length = 100f,
            material = "Твердый сплав",
            coating = "TiAlN",
            fluteCount = 2
        )
    ),
    ManufacturerTool(
        id = "KENNAMETAL_KCPM40",
        name = "Сверло Kennametal KCPM40",
        code = "KCPM40",
        type = "Сверло",
        manufacturer = "Kennametal",
        country = "США",
        specifications = ToolSpecifications(
            diameter = 8f,
            length = 80f,
            material = "Твердый сплав",
            coating = "TiN",
            pointAngle = 140f
        )
    ),
    ManufacturerTool(
        id = "WIDIA_M1200",
        name = "Фреза концевая Widia M1200",
        code = "M1200",
        type = "Концевая фреза",
        manufacturer = "Widia",
        country = "Германия",
        specifications = ToolSpecifications(
            diameter = 6f,
            length = 60f,
            material = "HSS",
            coating = "None",
            fluteCount = 3
        )
    ),
    ManufacturerTool(
        id = "ISCAR_HELIQ",
        name = "Фреза Iscar Heliq",
        code = "HELIQ",
        type = "Фреза",
        manufacturer = "Iscar",
        country = "Израиль",
        specifications = ToolSpecifications(
            diameter = 16f,
            length = 120f,
            material = "Твердый сплав",
            coating = "PVD",
            fluteCount = 5
        )
    )
)