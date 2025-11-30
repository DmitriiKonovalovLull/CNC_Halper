package com.konchak.cnc_halper.core.utils

import com.konchak.cnc_halper.domain.models.CuttingParameters

/**
 * Валидатор параметров резания
 * Проверяет безопасность, эффективность и оптимальность параметров
 */
class CuttingParametersValidator {

    companion object {

        /**
         * Получить уровень безопасности параметров
         */
        fun getSafetyLevel(parameters: CuttingParameters): SafetyLevel {
            return when {
                !parameters.areParametersSafe() -> SafetyLevel.DANGEROUS
                parameters.getEfficiency() < 0.3 -> SafetyLevel.LOW_EFFICIENCY
                parameters.getEfficiency() > 0.9 -> SafetyLevel.HIGH_LOAD
                parameters.powerRequirement > getMaxPowerForMaterial(parameters.material) -> SafetyLevel.HIGH_LOAD
                else -> SafetyLevel.OPTIMAL
            }
        }

        /**
         * Предложить улучшения для параметров
         */
        fun suggestImprovements(parameters: CuttingParameters): List<String> {
            val suggestions = mutableListOf<String>()
            val efficiency = parameters.getEfficiency()
            val safetyLevel = getSafetyLevel(parameters)

            when (safetyLevel) {
                SafetyLevel.DANGEROUS -> suggestions.add("❌ ОПАСНО: Параметры могут быть небезопасны для станка")
                SafetyLevel.HIGH_LOAD -> suggestions.add("⚠️ ВЫСОКАЯ НАГРУЗКА: Рекомендуется уменьшить скорость или подачу")
                SafetyLevel.LOW_EFFICIENCY -> suggestions.add("⚠️ НИЗКАЯ ЭФФЕКТИВНОСТЬ: Можно увеличить параметры резания")
                else -> {}
            }

            when {
                efficiency < 0.5 -> suggestions.add("📈 Увеличьте скорость резания на 20% для лучшей эффективности")
                efficiency > 0.8 -> suggestions.add("📉 Уменьшите скорость на 10% для увеличения стойкости инструмента")
            }

            if (!parameters.isSuitableForMaterial(parameters.material)) {
                suggestions.add("🔧 Параметры не оптимальны для материала ${parameters.material}")
            }

            // Проверка стойкости инструмента
            if (parameters.toolLife < getMinToolLifeForMaterial(parameters.material)) {
                suggestions.add("⏰ Ожидается быстрый износ инструмента")
            }

            // Проверка мощности
            val maxPower = getMaxPowerForMaterial(parameters.material)
            if (parameters.powerRequirement > maxPower * 0.8) {
                suggestions.add("⚡ Высокая потребляемая мощность (${parameters.powerRequirement} кВт)")
            }

            return suggestions
        }

        /**
         * Проверить совместимость параметров с материалом и инструментом
         */
        fun checkCompatibility(parameters: CuttingParameters): CompatibilityResult {
            val materialOk = parameters.isSuitableForMaterial(parameters.material)
            val safetyOk = parameters.areParametersSafe()
            val efficiency = parameters.getEfficiency()

            val warnings = mutableListOf<String>()
            val recommendations = mutableListOf<String>()

            if (!materialOk) {
                warnings.add("Параметры не оптимальны для материала ${parameters.material}")
            }

            if (!safetyOk) {
                warnings.add("Параметры могут быть небезопасны")
            }

            when {
                efficiency < 0.4 -> warnings.add("Очень низкая эффективность обработки")
                efficiency > 0.85 -> warnings.add("Очень высокая нагрузка на инструмент")
            }

            if (efficiency in 0.6f..0.8f) {
                recommendations.add("Эффективность в оптимальном диапазоне")
            }

            if (parameters.toolLife > 120) {
                recommendations.add("Отличная стойкость инструмента")
            }

            return CompatibilityResult(
                isCompatible = materialOk && safetyOk && efficiency in 0.3f..0.9f,
                warnings = warnings,
                recommendations = recommendations,
                efficiency = efficiency,
                safetyLevel = getSafetyLevel(parameters)
            )
        }

        /**
         * Оптимизировать параметры для целевой эффективности
         */
        fun optimizeForEfficiency(parameters: CuttingParameters, targetEfficiency: Float = 0.7f): CuttingParameters {
            var optimized = parameters
            val currentEfficiency = parameters.getEfficiency()

            return when {
                currentEfficiency < targetEfficiency - 0.1f -> {
                    // Увеличиваем скорость для повышения эффективности
                    optimized.withIncreasedSpeed(1.15f).copy(
                        recommendations = parameters.recommendations + "Скорость увеличена для лучшей эффективности"
                    )
                }
                currentEfficiency > targetEfficiency + 0.1f -> {
                    // Уменьшаем скорость для снижения износа
                    optimized.withDecreasedSpeed(0.85f).copy(
                        recommendations = parameters.recommendations + "Скорость уменьшена для увеличения стойкости"
                    )
                }
                else -> optimized.copy(
                    recommendations = parameters.recommendations + "Параметры уже оптимальны"
                )
            }
        }

        /**
         * Создать детальный отчет по параметрам
         */
        fun createDetailedReport(parameters: CuttingParameters): Map<String, Any> {
            val baseReport = parameters.toReportMap()
            val compatibility = checkCompatibility(parameters)
            val safetyLevel = getSafetyLevel(parameters)
            val improvements = suggestImprovements(parameters)

            return baseReport + mapOf(
                "validation" to mapOf(
                    "safetyLevel" to safetyLevel.name,
                    "isSafe" to parameters.areParametersSafe(),
                    "isOptimal" to compatibility.isCompatible,
                    "efficiency" to parameters.getEfficiency()
                ),
                "compatibility" to mapOf(
                    "warnings" to compatibility.warnings,
                    "recommendations" to compatibility.recommendations
                ),
                "improvements" to improvements,
                "formattedSummary" to createFormattedSummary(parameters, compatibility, safetyLevel)
            )
        }

        // Вспомогательные методы

        private fun getMaxPowerForMaterial(material: String): Float {
            return when (material.lowercase()) {
                "алюминий", "aluminum" -> 3.0f
                "сталь", "steel" -> 7.0f
                "нержавейка", "stainless" -> 10.0f
                "титан", "titanium" -> 12.0f
                else -> 5.0f
            }
        }

        private fun getMinToolLifeForMaterial(material: String): Int {
            return when (material.lowercase()) {
                "алюминий", "aluminum" -> 90
                "сталь", "steel" -> 45
                "нержавейка", "stainless" -> 30
                "титан", "titanium" -> 20
                else -> 60
            }
        }

        private fun createFormattedSummary(
            parameters: CuttingParameters,
            compatibility: CompatibilityResult,
            safetyLevel: SafetyLevel
        ): String {
            return buildString {
                append("📊 ОТЧЕТ ПО ПАРАМЕТРАМ РЕЗАНИЯ\n\n")

                append("🛠️ ИНСТРУМЕНТ: ${parameters.toolType} | ${parameters.material}\n")
                append("⚡ ЭФФЕКТИВНОСТЬ: ${"%.1f".format(parameters.getEfficiency() * 100)}%\n")
                append("🛡️ БЕЗОПАСНОСТЬ: ${safetyLevel.emoji} ${safetyLevel.displayName}\n\n")

                if (compatibility.warnings.isNotEmpty()) {
                    append("⚠️ ПРЕДУПРЕЖДЕНИЯ:\n")
                    compatibility.warnings.forEach { append("• $it\n") }
                    append("\n")
                }

                if (compatibility.recommendations.isNotEmpty()) {
                    append("💡 РЕКОМЕНДАЦИИ:\n")
                    compatibility.recommendations.forEach { append("• $it\n") }
                }
            }
        }
    }

    enum class SafetyLevel(val emoji: String, val displayName: String) {
        OPTIMAL("✅", "Оптимально"),
        LOW_EFFICIENCY("🟡", "Низкая эффективность"),
        HIGH_LOAD("🟠", "Высокая нагрузка"),
        DANGEROUS("🔴", "Опасно")
    }

    data class CompatibilityResult(
        val isCompatible: Boolean,
        val warnings: List<String>,
        val recommendations: List<String>,
        val efficiency: Float,
        val safetyLevel: SafetyLevel
    )
}