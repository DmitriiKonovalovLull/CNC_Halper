package com.konchak.cnc_halper.domain.models

data class CuttingParameters(
    val cuttingSpeed: Float, // м/мин
    val feedRate: Float,     // мм/зуб
    val depthOfCut: Float,   // мм
    val recommendations: List<String>
)