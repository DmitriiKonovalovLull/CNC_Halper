package com.konchak.cnc_halper.domain.models

import kotlin.math.PI
import kotlin.math.abs

/**
 * Параметры резания для инструмента
 * Поддержка фрезерных и токарных операций
 */
data class CuttingParameters(
    val toolId: String = "",
    val toolType: ToolType = ToolType.MILLING, // Тип инструмента
    val material: String = "", // Обрабатываемый материал
    val cuttingSpeed: Float, // м/мин - скорость резания
    val feedRate: Float,     // мм/зуб (фреза) или мм/об (токарная) - подача
    val depthOfCut: Float,   // мм - глубина резания
    val widthOfCut: Float = 0f, // мм - ширина резания (для фрезерования)
    val rpm: Int = 0,        // об/мин - расчетные обороты шпинделя
    val feedPerMinute: Float = 0f, // мм/мин - минутная подача
    val coolant: String = "Рекомендуется", // Рекомендации по СОЖ
    val powerRequirement: Float = 0f, // кВт - требуемая мощность
    val surfaceFinish: String = "", // Ожидаемое качество поверхности
    val toolLife: Int = 0,   // мин - ожидаемый срок службы
    val recommendations: List<String> = emptyList(), // Дополнительные рекомендации
    val source: String = "manufacturer", // manufacturer, ai, experience

    // Токарные специфичные параметры
    val turningDiameter: Float = 0f, // мм - диаметр заготовки
    val cuttingLength: Float = 0f,   // мм - длина резания
    val approachAngle: Float = 0f,   // градусы - угол подхода
    val noseRadius: Float = 0.4f,    // мм - радиус привершинки
) : Comparable<CuttingParameters> { // ✅ ДОБАВЛЕНО Comparable

    enum class ToolType {
        MILLING,    // Фрезерование
        TURNING,    // Токарная обработка
        DRILLING,   // Сверление
        TAPPING     // Нарезка резьбы
    }

    // Расчет оборотов шпинделя для фрезерования
    fun calculateMillingRpm(toolDiameter: Float): Int {
        return if (toolDiameter > 0) {
            (cuttingSpeed * 1000 / (PI * toolDiameter)).toInt()
        } else {
            0
        }
    }

    // Расчет оборотов шпинделя для токарной обработки
    fun calculateTurningRpm(workpieceDiameter: Float): Int {
        return if (workpieceDiameter > 0) {
            (cuttingSpeed * 1000 / (PI * workpieceDiameter)).toInt()
        } else {
            0
        }
    }

    // Расчет минутной подачи для фрезерования
    fun calculateMillingFeedPerMinute(fluteCount: Int): Float {
        return feedRate * fluteCount * rpm
    }

    // Расчет минутной подачи для токарной обработки
    fun calculateTurningFeedPerMinute(): Float {
        return feedRate * rpm
    }

    // Расчет времени обработки для токарной операции
    fun calculateTurningTime(length: Float): Float {
        return if (feedPerMinute > 0) length / feedPerMinute else 0f
    }

    // Расчет объема снимаемого материала для токарной обработки
    fun calculateTurningVolume(diameterBefore: Float, diameterAfter: Float, length: Float): Float {
        val radiusBefore = diameterBefore / 2
        val radiusAfter = diameterAfter / 2
        return (PI * (radiusBefore * radiusBefore - radiusAfter * radiusAfter) * length).toFloat()
    }

    // Расчет силы резания
    fun calculateCuttingForce(material: String): Float {
        val materialFactor = when (material.lowercase()) {
            "алюминий", "aluminum" -> 500f
            "латунь", "brass" -> 700f
            "медь", "copper" -> 900f
            "сталь", "steel" -> 1500f
            "нержавейка", "stainless" -> 2000f
            else -> 1000f
        }
        return depthOfCut * feedRate * materialFactor * 0.001f
    }

    // Расчет крутящего момента
    fun calculateTorque(toolDiameter: Float): Float {
        val cuttingForce = calculateCuttingForce(material)
        return cuttingForce * toolDiameter / 2000
    }

    // Проверка безопасности параметров
    fun areParametersSafe(): Boolean {
        return cuttingSpeed > 0 && feedRate > 0 && depthOfCut > 0
    }

    // Получить параметры в текстовом формате
    fun getFormattedParameters(): String {
        return buildString {
            append("📊 ПАРАМЕТРЫ РЕЗАНИЯ ($toolType):\n\n")

            when (toolType) {
                ToolType.MILLING -> {
                    append("• Скорость резания: $cuttingSpeed м/мин\n")
                    append("• Подача на зуб: $feedRate мм/зуб\n")
                    append("• Глубина резания: $depthOfCut мм\n")
                    append("• Ширина резания: $widthOfCut мм\n")
                }
                ToolType.TURNING -> {
                    append("• Скорость резания: $cuttingSpeed м/мин\n")
                    append("• Подача на оборот: $feedRate мм/об\n")
                    append("• Глубина резания: $depthOfCut мм\n")
                    append("• Диаметр заготовки: $turningDiameter мм\n")
                    append("• Радиус привершинки: $noseRadius мм\n")
                }
                else -> {
                    append("• Скорость резания: $cuttingSpeed м/мин\n")
                    append("• Подача: $feedRate мм/об\n")
                    append("• Глубина резания: $depthOfCut мм\n")
                }
            }

            append("• Обороты шпинделя: $rpm об/мин\n")
            append("• Минутная подача: $feedPerMinute мм/мин\n")
            append("• СОЖ: $coolant\n")
            append("• Мощность: $powerRequirement кВт\n")
            append("• Срок службы инструмента: $toolLife мин\n\n")

            append("💡 РЕКОМЕНДАЦИИ:\n")
            recommendations.forEach { append("• $it\n") }
        }
    }

    // Сравнение с другими параметрами
    fun isSimilarTo(other: CuttingParameters, tolerance: Float = 0.1f): Boolean {
        return abs(cuttingSpeed - other.cuttingSpeed) / cuttingSpeed <= tolerance &&
                abs(feedRate - other.feedRate) / feedRate <= tolerance
    }

    // ✅ ДОБАВЛЕНО: Реализация Comparable для сортировки
    override operator fun compareTo(other: CuttingParameters): Int {
        // Сравниваем по производительности (скорость резания * подача)
        val thisPerformance = cuttingSpeed * feedRate
        val otherPerformance = other.cuttingSpeed * other.feedRate

        return when {
            thisPerformance > otherPerformance -> 1
            thisPerformance < otherPerformance -> -1
            else -> 0
        }
    }

    // ✅ ДОБАВЛЕНО: Дополнительные полезные методы

    // Получить эффективность обработки (0.0 - 1.0)
    fun getEfficiency(): Float {
        val maxSpeed = when (material.lowercase()) {
            "алюминий", "aluminum" -> 300f
            "сталь", "steel" -> 120f
            "нержавейка", "stainless" -> 80f
            "латунь", "brass" -> 200f
            else -> 150f
        }
        return (cuttingSpeed / maxSpeed).coerceIn(0f, 1f)
    }

    // Проверить, подходит ли для материала
    fun isSuitableForMaterial(targetMaterial: String): Boolean {
        return when (targetMaterial.lowercase()) {
            "алюминий", "aluminum" -> cuttingSpeed <= 300f && feedRate <= 0.3f
            "сталь", "steel" -> cuttingSpeed <= 120f && feedRate <= 0.15f
            "нержавейка", "stainless" -> cuttingSpeed <= 80f && feedRate <= 0.12f
            else -> true
        }
    }

    // Создать копию с увеличенной скоростью
    fun withIncreasedSpeed(multiplier: Float): CuttingParameters {
        return copy(
            cuttingSpeed = cuttingSpeed * multiplier,
            rpm = (rpm * multiplier).toInt(),
            feedPerMinute = feedPerMinute * multiplier
        )
    }

    // Создать копию с уменьшенной скоростью
    fun withDecreasedSpeed(multiplier: Float): CuttingParameters {
        return copy(
            cuttingSpeed = cuttingSpeed * multiplier,
            rpm = (rpm * multiplier).toInt(),
            feedPerMinute = feedPerMinute * multiplier
        )
    }

    // Получить параметры для отчета
    fun toReportMap(): Map<String, Any> {
        return mapOf(
            "toolId" to toolId,
            "toolType" to toolType.name,
            "material" to material,
            "cuttingSpeed" to cuttingSpeed,
            "feedRate" to feedRate,
            "depthOfCut" to depthOfCut,
            "rpm" to rpm,
            "feedPerMinute" to feedPerMinute,
            "powerRequirement" to powerRequirement,
            "toolLife" to toolLife,
            "efficiency" to getEfficiency()
        )
    }
}