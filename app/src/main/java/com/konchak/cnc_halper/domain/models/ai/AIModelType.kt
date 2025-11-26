package com.konchak.cnc_halper.domain.models.ai

enum class AIModelType {
    MiniTFLite,    // Локальная TensorFlow Lite модель
    CloudGPT,      // Облачная GPT модель
    CloudGPT3,     // Облачная GPT-3 модель
    CloudGPT4,     // Облачная GPT-4 модель
    Hybrid,        // Гибридный режим
    Offline        // Полностью оффлайн
}
