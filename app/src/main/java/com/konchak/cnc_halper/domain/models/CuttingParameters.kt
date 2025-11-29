package com.konchak.cnc_halper.domain.models

import kotlin.math.abs

/**
 * Параметры резания для инструмента
 * На основе рекомендаций производителя и материала обработки
 */
data class CuttingParameters(
    val toolId: String = "",
    val material: String = "", // Обрабатываемый материал
    val cuttingSpeed: Float, // м/мин - скорость резания
    val feedRate: Float,     // мм/зуб - подача на зуб
    val depthOfCut: Float,   // мм - глубина резания
    val widthOfCut: Float = 0f, // мм - ширина резания
    val rpm: Int = 0,        // об/мин - расчетные обороты шпинделя
    val feedPerMinute: Float = 0f, // мм/мин - минутная подача
    val coolant: String = "Рекомендуется", // Рекомендации по СОЖ
    val powerRequirement: Float = 0f, // кВт - требуемая мощность
    val surfaceFinish: String = "", // Ожидаемое качество поверхности
    val toolLife: Int = 0,   // мин - ожидаемый срок службы
    val recommendations: List<String> = emptyList(), // Дополнительные рекомендации
    val source: String = "manufacturer" // manufacturer, ai, experience
) {

    // Расчет оборотов шпинделя на основе диаметра инструмента
    @Suppress("unused")
    fun calculateRpm(toolDiameter: Float): Int {
        return if (toolDiameter > 0) {
            (cuttingSpeed * 1000 / (kotlin.math.PI * toolDiameter)).toInt()
        } else {
            0
        }
    }

    // Расчет минутной подачи на основе количества зубьев
    @Suppress("unused")
    fun calculateFeedPerMinute(fluteCount: Int): Float {
        return feedRate * fluteCount * rpm
    }

    // Проверка безопасности параметров
    @Suppress("unused")
    fun areParametersSafe(): Boolean {
        return cuttingSpeed > 0 && feedRate > 0 && depthOfCut > 0
    }

    // Получить параметры в текстовом формате
    @Suppress("unused")
    fun getFormattedParameters(): String {
        return """
            📊 ПАРАМЕТРЫ РЕЗАНИЯ:
            
            • Скорость резания: $cuttingSpeed м/мин
            • Подача на зуб: $feedRate мм/зуб
            • Глубина резания: $depthOfCut мм
            • Ширина резания: ${widthOfCut}мм
            • Обороты шпинделя: $rpm об/мин
            • Минутная подача: $feedPerMinute мм/мин
            • СОЖ: $coolant
            • Мощность: $powerRequirement кВт
            
            💡 РЕКОМЕНДАЦИИ:
            ${recommendations.joinToString("\n• ")}
        """.trimIndent()
    }

    // Сравнение с другими параметрами
    @Suppress("unused")
    fun isSimilarTo(other: CuttingParameters, tolerance: Float = 0.1f): Boolean {
        return abs(cuttingSpeed - other.cuttingSpeed) / cuttingSpeed <= tolerance &&
                abs(feedRate - other.feedRate) / feedRate <= tolerance
    }
}