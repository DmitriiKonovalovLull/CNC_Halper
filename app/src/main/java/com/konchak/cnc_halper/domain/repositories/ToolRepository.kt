package com.konchak.cnc_halper.domain.repositories

import com.konchak.cnc_halper.domain.models.Tool
import com.konchak.cnc_halper.domain.models.ManufacturerTool
import com.konchak.cnc_halper.domain.models.ToolAnalysisResult
import com.konchak.cnc_halper.domain.models.CuttingParameters
import kotlinx.coroutines.flow.Flow

interface ToolRepository {

    // === ЛОКАЛЬНЫЕ ОПЕРАЦИИ (Room) ===

    /** Сохранить инструмент в локальную БД */
    suspend fun saveTool(
        operatorId: Long,
        name: String,
        type: String,
        size: String,
        photoPath: String?
    ): Long

    /** Получить инструменты оператора */
    fun getToolsByOperator(operatorId: Long): Flow<List<Tool>>

    /** Получить все инструменты */
    fun getAllTools(): Flow<List<Tool>>

    /** Получить инструмент по ID */
    suspend fun getToolById(id: Long): Tool?

    /** Обновить инструмент */
    suspend fun updateTool(tool: Tool)

    /** Удалить инструмент */
    suspend fun deleteTool(id: Long)

    /** Получить количество инструментов оператора */
    suspend fun getToolsCount(operatorId: Long): Int

    // === ПОИСК В БАЗАХ ПРОИЗВОДИТЕЛЕЙ ===

    /** Поиск инструментов по запросу */
    suspend fun searchTools(query: String): List<ManufacturerTool>

    /** Поиск инструмента по коду производителя */
    suspend fun getToolByCode(code: String): ManufacturerTool?

    /** Получить похожие инструменты */
    suspend fun getSimilarTools(tool: Tool): List<ManufacturerTool>

    // === СИНХРОНИЗАЦИЯ С FIREBASE ===

    /** Синхронизировать данные с Firebase */
    suspend fun syncData(): Boolean

    /** Загрузить фото инструмента в Firebase Storage */
    suspend fun uploadToolImage(imagePath: String): String?

    /** Получить несинхронизированные инструменты */
    suspend fun getUnsyncedTools(): List<Tool>

    /** Сканировать инструмент через камеру */
    suspend fun scanTool(imagePath: String): Tool

    /** Проанализировать износ инструмента */
    suspend fun analyzeToolWear(toolId: String): ToolAnalysisResult

    /** Получить рекомендации по режимам резания */
    suspend fun getCuttingRecommendations(tool: Tool, material: String): CuttingParameters

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===

    /** Получить инструменты (Flow версия) */
    fun getTools(): Flow<List<Tool>>

    /** Обновить уровень износа инструмента */
    suspend fun updateWearLevel(toolId: Long, wearLevel: Int)

    /** Отметить использование инструмента */
    suspend fun markToolUsed(toolId: Long, machineId: String)

    suspend fun addTool(tool: Tool)
    suspend fun deleteTool(tool: Tool)
}