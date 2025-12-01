package com.konchak.cnc_halper.domain.models.ai

enum class AIModelType {
    MiniTFLite,    // ← можно это (локальный ИИ)
    CloudGPT,      // ← облачный
    Hybrid,        // ← гибридный (рекомендую)
    CloudGPT4      // ← другой облачный
}