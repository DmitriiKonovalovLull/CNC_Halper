package com.konchak.cnc_halper.domain.usecases.ai

import com.konchak.cnc_halper.domain.models.AIResponse
import com.konchak.cnc_halper.domain.models.CuttingParameters
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel // <-- Import added
import com.konchak.cnc_halper.domain.repositories.AIRepository
import kotlin.math.PI
import javax.inject.Inject

class PredictCuttingParamsUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {

    // Main method for predicting parameters
    suspend operator fun invoke(
        material: String,
        toolType: String,
        operation: String,
        workpieceDiameter: Float = 0f,
        toolDiameter: Float = 0f,
        turningLength: Float = 0f
    ): CuttingParameters {
        val query = buildString {
            append("Recommend cutting parameters for: material=$material, tool=$toolType, operation=$operation")
            if (workpieceDiameter > 0) append(", workpiece diameter=${workpieceDiameter}mm")
            if (toolDiameter > 0) append(", tool diameter=${toolDiameter}mm")
            if (turningLength > 0) append(", machining length=${turningLength}mm")
        }

        return when (val response = aiRepository.processWithHybridAI(query)) {
            is AIResponse.Success -> parseCuttingParamsFromResponse(
                response = response.answer,
                material = material,
                toolType = toolType,
                operation = operation,
                workpieceDiameter = workpieceDiameter,
                toolDiameter = toolDiameter,
                turningLength = turningLength
            )
            is AIResponse.Error -> getDefaultCuttingParameters(
                material = material,
                toolType = toolType,
                operation = operation,
                workpieceDiameter = workpieceDiameter,
                toolDiameter = toolDiameter,
                turningLength = turningLength
            )
        }
    }

    // Method to get information about the AI model
    suspend fun getModelInfo(): MiniAIModel? = aiRepository.getModelInfo()

    // Method to get tool capabilities by tool type
    fun getToolCapabilities(toolType: String): List<String> {
        return when {
            toolType.contains("milling cutter", ignoreCase = true) -> listOf(
                "Cutting speed calculation: 80-300 m/min",
                "Feed rate recommendations: 0.05-0.3 mm/tooth",
                "Depth of cut: 0.5-5 mm",
                "Width of cut: 0.3-0.8 of diameter"
            )
            toolType.contains("drill", ignoreCase = true) -> listOf(
                "Drilling speed: 20-60 m/min",
                "Feed rate: 0.02-0.15 mm/rev",
                "Drilling depth: up to 10×diameter",
                "Coolant: mandatory for steel"
            )
            toolType.contains("lathe tool", ignoreCase = true) -> listOf(
                "Turning speed: 50-250 m/min",
                "Feed rate: 0.1-0.4 mm/rev",
                "Depth of cut: 0.5-6 mm",
                "Approach angle: 45-95°"
            )
            toolType.contains("tap", ignoreCase = true) -> listOf(
                "Tapping speed: 5-15 m/min",
                "Feed rate: equal to thread pitch",
                "Coolant: mandatory",
                "Tips to prevent breakage"
            )
            else -> listOf(
                "Basic recommendations for cutting modes",
                "Parameter selection for standard materials",
                "Spindle speed calculation"
            )
        }
    }

    // Method to check if a tool type is supported
    fun isToolTypeSupported(toolType: String): Boolean {
        val supportedTypes = listOf(
            "milling cutter", "end mill", "drill", "lathe tool",
            "tap", "countersink", "reamer"
        )
        return supportedTypes.any { toolType.contains(it, ignoreCase = true) }
    }

    // Method to get recommended materials for a tool type
    fun getRecommendedMaterials(toolType: String): List<String> {
        return when {
            toolType.contains("milling cutter", ignoreCase = true) -> listOf(
                "Aluminum", "Steel", "Stainless Steel", "Brass", "Plastic"
            )
            toolType.contains("drill", ignoreCase = true) -> listOf(
                "Steel", "Aluminum", "Cast Iron", "Non-ferrous metals"
            )
            toolType.contains("lathe tool", ignoreCase = true) -> listOf(
                "Steel", "Stainless Steel", "Aluminum", "Brass", "Titanium"
            )
            else -> listOf("Steel", "Aluminum", "Stainless Steel")
        }
    }

    private fun parseCuttingParamsFromResponse(
        response: String,
        material: String,
        @Suppress("unused") toolType: String, // Added @Suppress("unused") back
        operation: String,
        workpieceDiameter: Float,
        toolDiameter: Float,
        turningLength: Float
    ): CuttingParameters {
        val cuttingToolType = detectToolType(toolType)
        val cuttingSpeed = extractValue(response, "cutting speed", getDefaultSpeed(material, cuttingToolType))
        val feedRate = extractValue(response, "feed", getDefaultFeed(toolType, cuttingToolType))
        val depthOfCut = extractValue(response, "depth of cut", getDefaultDepth(operation, cuttingToolType))

        // Calculated parameters depending on the tool type
        val (rpm, feedPerMinute) = when (cuttingToolType) {
            CuttingParameters.ToolType.TURNING -> {
                val effectiveDiameter = if (workpieceDiameter > 0) workpieceDiameter else getDefaultWorkpieceDiameter()
                val calculatedRpm = calculateTurningRpm(cuttingSpeed, effectiveDiameter)
                val calculatedFeed = calculateTurningFeedPerMinute(feedRate, calculatedRpm)
                calculatedRpm to calculatedFeed
            }
            else -> {
                val effectiveDiameter = if (toolDiameter > 0) toolDiameter else getDefaultDiameter(toolType)
                val calculatedRpm = calculateMillingRpm(cuttingSpeed, effectiveDiameter)
                val fluteCount = getDefaultFluteCount(toolType)
                val calculatedFeed = calculateMillingFeedPerMinute(feedRate, fluteCount, calculatedRpm)
                calculatedRpm to calculatedFeed
            }
        }

        val powerRequirement = calculatePowerRequirement(material, cuttingSpeed, feedRate, depthOfCut, cuttingToolType)

        return CuttingParameters(
            toolId = generateToolId(toolType),
            toolType = cuttingToolType,
            material = material,
            cuttingSpeed = cuttingSpeed,
            feedRate = feedRate,
            depthOfCut = depthOfCut,
            widthOfCut = getDefaultWidth(operation, cuttingToolType),
            rpm = rpm,
            feedPerMinute = feedPerMinute,
            coolant = getCoolantRecommendation(material),
            powerRequirement = powerRequirement,
            surfaceFinish = getSurfaceFinish(operation),
            toolLife = getToolLife(material, toolType, cuttingToolType),
            recommendations = extractRecommendations(response, material),
            source = "ai",
            turningDiameter = workpieceDiameter,
            cuttingLength = turningLength,
            approachAngle = getDefaultApproachAngle(toolType),
            noseRadius = getDefaultNoseRadius(toolType)
        )
    }

    private fun extractValue(text: String, key: String, defaultValue: Float): Float {
        val patterns = listOf(
            "$key[\\s:]*([0-9]+(?:\\.[0-9]+)?)".toRegex(RegexOption.IGNORE_CASE),
            "$key\\s*=\\s*([0-9]+(?:\\.[0-9]+)?)".toRegex(RegexOption.IGNORE_CASE),
            "([0-9]+(?:\\.[0-9]+)?)\\s*(?:m/min|mm/tooth|mm/rev|mm|mm/min)".toRegex(RegexOption.IGNORE_CASE)
        )

        for (pattern in patterns) {
            val match = pattern.find(text)
            if (match != null) {
                return match.groupValues[1].toFloatOrNull() ?: defaultValue
            }
        }
        return defaultValue
    }

    private fun extractRecommendations(text: String, material: String): List<String> {
        val recommendations = mutableListOf<String>()

        val lines = text.split("\n", ". ", "; ")
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.length > 15 && (
                        trimmed.contains("recommend", ignoreCase = true) ||
                                trimmed.contains("advice", ignoreCase = true) ||
                                trimmed.contains("important", ignoreCase = true) ||
                                trimmed.contains("note", ignoreCase = true)
                        )) {
                recommendations.add(trimmed)
            }
        }

        return if (recommendations.isEmpty()) {
            getDefaultRecommendations(material)
        } else {
            recommendations.take(3)
        }
    }

    private fun getDefaultCuttingParameters(
        material: String,
        toolType: String,
        operation: String,
        workpieceDiameter: Float,
        toolDiameter: Float,
        turningLength: Float
    ): CuttingParameters {
        val cuttingToolType = detectToolType(toolType)
        val cuttingSpeed = getDefaultSpeed(material, cuttingToolType)
        val feedRate = getDefaultFeed(toolType, cuttingToolType)
        val depthOfCut = getDefaultDepth(operation, cuttingToolType)

        val (rpm, feedPerMinute) = when (cuttingToolType) {
            CuttingParameters.ToolType.TURNING -> {
                val effectiveDiameter = if (workpieceDiameter > 0) workpieceDiameter else getDefaultWorkpieceDiameter()
                val calculatedRpm = calculateTurningRpm(cuttingSpeed, effectiveDiameter)
                val calculatedFeed = calculateTurningFeedPerMinute(feedRate, calculatedRpm)
                calculatedRpm to calculatedFeed
            }
            else -> {
                val effectiveDiameter = if (toolDiameter > 0) toolDiameter else getDefaultDiameter(toolType)
                val calculatedRpm = calculateMillingRpm(cuttingSpeed, effectiveDiameter)
                val fluteCount = getDefaultFluteCount(toolType)
                val calculatedFeed = calculateMillingFeedPerMinute(feedRate, fluteCount, calculatedRpm)
                calculatedRpm to calculatedFeed
            }
        }

        val powerRequirement = calculatePowerRequirement(material, cuttingSpeed, feedRate, depthOfCut, cuttingToolType)

        return CuttingParameters(
            toolId = generateToolId(toolType),
            toolType = cuttingToolType,
            material = material,
            cuttingSpeed = cuttingSpeed,
            feedRate = feedRate,
            depthOfCut = depthOfCut,
            widthOfCut = getDefaultWidth(operation, cuttingToolType),
            rpm = rpm,
            feedPerMinute = feedPerMinute,
            coolant = getCoolantRecommendation(material),
            powerRequirement = powerRequirement,
            surfaceFinish = getSurfaceFinish(operation),
            toolLife = getToolLife(material, toolType, cuttingToolType),
            recommendations = getDefaultRecommendations(material),
            source = "default",
            turningDiameter = workpieceDiameter,
            cuttingLength = turningLength,
            approachAngle = getDefaultApproachAngle(toolType),
            noseRadius = getDefaultNoseRadius(toolType)
        )
    }

    // Helper methods for calculations

    private fun calculateMillingRpm(cuttingSpeed: Float, toolDiameter: Float): Int {
        return if (toolDiameter > 0) {
            (cuttingSpeed * 1000 / (PI * toolDiameter)).toInt()
        } else {
            0
        }
    }

    private fun calculateTurningRpm(cuttingSpeed: Float, workpieceDiameter: Float): Int {
        return if (workpieceDiameter > 0) {
            (cuttingSpeed * 1000 / (PI * workpieceDiameter)).toInt()
        } else {
            0
        }
    }

    private fun calculateMillingFeedPerMinute(feedRate: Float, fluteCount: Int, rpm: Int): Float {
        return feedRate * fluteCount * rpm
    }

    private fun calculateTurningFeedPerMinute(feedRate: Float, rpm: Int): Float {
        return feedRate * rpm
    }

    private fun calculatePowerRequirement(
        material: String,
        cuttingSpeed: Float,
        feedRate: Float,
        depthOfCut: Float,
        toolType: CuttingParameters.ToolType
    ): Float {
        val materialFactor = when (material.lowercase()) {
            "aluminum" -> 0.3f
            "brass" -> 0.4f
            "copper" -> 0.5f
            "steel" -> 0.8f
            "stainless steel" -> 1.0f
            else -> 0.6f
        }

        val toolTypeFactor = when (toolType) {
            CuttingParameters.ToolType.TURNING -> 1.2f
            CuttingParameters.ToolType.DRILLING -> 1.5f
            else -> 1.0f
        }

        return cuttingSpeed * feedRate * depthOfCut * materialFactor * toolTypeFactor * 0.01f
    }

    // Determine tool type by name
    private fun detectToolType(toolType: String): CuttingParameters.ToolType {
        return when {
            toolType.contains("milling cutter", ignoreCase = true) ||
                    toolType.contains("end mill", ignoreCase = true) -> CuttingParameters.ToolType.MILLING

            toolType.contains("lathe tool", ignoreCase = true) ||
                    toolType.contains("turning", ignoreCase = true) -> CuttingParameters.ToolType.TURNING

            toolType.contains("drill", ignoreCase = true) -> CuttingParameters.ToolType.DRILLING

            toolType.contains("tap", ignoreCase = true) -> CuttingParameters.ToolType.TAPPING

            else -> CuttingParameters.ToolType.MILLING
        }
    }

    private fun generateToolId(toolType: String): String {
        val detectedType = detectToolType(toolType)
        return when (detectedType) {
            CuttingParameters.ToolType.MILLING -> "EM_${toolType.hashCode()}"
            CuttingParameters.ToolType.TURNING -> "LT_${toolType.hashCode()}"
            CuttingParameters.ToolType.DRILLING -> "DR_${toolType.hashCode()}"
            CuttingParameters.ToolType.TAPPING -> "TP_${toolType.hashCode()}"
        }
    }

    private fun getDefaultSpeed(material: String, toolType: CuttingParameters.ToolType): Float {
        val baseSpeed = when (material.lowercase()) {
            "aluminum" -> 200f
            "steel" -> 80f
            "stainless steel" -> 60f
            "brass" -> 150f
            "copper" -> 100f
            else -> 120f
        }

        // Adjust speed for different processing types
        return when (toolType) {
            CuttingParameters.ToolType.TURNING -> baseSpeed * 1.1f
            CuttingParameters.ToolType.DRILLING -> baseSpeed * 0.8f
            CuttingParameters.ToolType.TAPPING -> baseSpeed * 0.5f
            else -> baseSpeed
        }
    }

    private fun getDefaultFeed(toolType: String, cuttingToolType: CuttingParameters.ToolType): Float {
        return when {
            cuttingToolType == CuttingParameters.ToolType.TURNING -> {
                when (toolType.lowercase()) {
                    "roughing tool" -> 0.3f
                    "finishing tool" -> 0.1f
                    "boring tool" -> 0.15f
                    else -> 0.2f
                }
            }
            else -> {
                when (toolType.lowercase()) {
                    "milling cutter", "end mill" -> 0.1f
                    "drill" -> 0.05f
                    "lathe tool" -> 0.2f
                    else -> 0.15f
                }
            }
        }
    }

    private fun getDefaultDepth(operation: String, toolType: CuttingParameters.ToolType): Float {
        return when (toolType) {
            CuttingParameters.ToolType.TURNING -> {
                when (operation.lowercase()) {
                    "roughing" -> 3.0f
                    "finishing" -> 0.5f
                    else -> 2.0f
                }
            }
            else -> {
                when (operation.lowercase()) {
                    "roughing" -> 2.0f
                    "finishing" -> 0.5f
                    "drilling" -> 5.0f
                    else -> 1.0f
                }
            }
        }
    }

    private fun getDefaultWidth(operation: String, toolType: CuttingParameters.ToolType): Float {
        return when (toolType) {
            CuttingParameters.ToolType.MILLING -> {
                when (operation.lowercase()) {
                    "roughing" -> 0.8f
                    "finishing" -> 0.3f
                    else -> 0.5f
                }
            }
            else -> 0f
        }
    }

    private fun getDefaultDiameter(toolType: String): Float {
        return when (toolType.lowercase()) {
            "milling cutter", "end mill" -> 10f
            "drill" -> 8f
            "lathe tool" -> 12f
            else -> 10f
        }
    }

    private fun getDefaultWorkpieceDiameter(): Float = 50f

    private fun getDefaultFluteCount(toolType: String): Int {
        return when (toolType.lowercase()) {
            "milling cutter", "end mill" -> 4
            "drill" -> 2
            "lathe tool" -> 1
            else -> 2
        }
    }

    private fun getDefaultApproachAngle(toolType: String): Float {
        return when {
            toolType.contains("roughing", ignoreCase = true) -> 45f
            toolType.contains("finishing", ignoreCase = true) -> 90f
            toolType.contains("boring", ignoreCase = true) -> 95f
            else -> 75f
        }
    }

    private fun getDefaultNoseRadius(toolType: String): Float {
        return when {
            toolType.contains("roughing", ignoreCase = true) -> 0.8f
            toolType.contains("finishing", ignoreCase = true) -> 0.4f
            toolType.contains("boring", ignoreCase = true) -> 0.2f
            else -> 0.4f
        }
    }

    private fun getCoolantRecommendation(material: String): String {
        return when (material.lowercase()) {
            "aluminum" -> "Mandatory"
            "stainless steel" -> "Mandatory"
            else -> "Recommended"
        }
    }

    private fun getSurfaceFinish(operation: String): String {
        return when (operation.lowercase()) {
            "finishing" -> "High"
            "roughing" -> "Medium"
            else -> "Normal"
        }
    }

    private fun getToolLife(material: String, toolType: String, cuttingToolType: CuttingParameters.ToolType): Int {
        val baseLife = when {
            material.contains("stainless steel", ignoreCase = true) -> 45
            material.contains("steel", ignoreCase = true) -> 60
            material.contains("aluminum", ignoreCase = true) -> 120
            else -> 90
        }

        // Adjust tool life for turning tools
        return when (cuttingToolType) {
            CuttingParameters.ToolType.TURNING -> (baseLife * 1.5).toInt()
            else -> baseLife
        }
    }

    private fun getDefaultRecommendations(material: String): List<String> {
        return listOf(
            "Use coolant: ${getCoolantRecommendation(material)}",
            "Check tool sharpness before operation",
            "Monitor machine vibrations"
        )
    }
}
