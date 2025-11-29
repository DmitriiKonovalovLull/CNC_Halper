package com.konchak.cnc_halper.domain.models

/**
 * Детальные технические характеристики инструмента
 * Используется для расширенного описания инструментов
 */
data class ToolSpecification(
    val id: String = "",
    val toolId: String = "", // Ссылка на основной инструмент
    val standard: String = "", // ГОСТ, ISO, DIN стандарт
    val weight: Float = 0f, // Вес в граммах
    val balanceQuality: String = "", // Качество балансировки
    val maxRpm: Int = 0, // Максимальные обороты
    val temperatureRange: String = "", // Рабочий температурный диапазон
    val certifications: List<String> = emptyList(), // Сертификаты качества
    val safetyWarnings: List<String> = emptyList(), // Предупреждения по безопасности
    val maintenanceSchedule: MaintenanceSchedule = MaintenanceSchedule(),
    val compatibility: List<String> = emptyList() // Совместимые станки/оснастка
)

/**
 * График технического обслуживания инструмента
 */
data class MaintenanceSchedule(
    val sharpeningInterval: Int = 0, // Интервал заточки в часах
    val inspectionInterval: Int = 0, // Интервал проверки в часах
    val replacementInterval: Int = 0, // Интервал замены в часах
    val lastMaintenance: Long = 0, // Дата последнего ТО
    val nextMaintenance: Long = 0 // Дата следующего ТО
)