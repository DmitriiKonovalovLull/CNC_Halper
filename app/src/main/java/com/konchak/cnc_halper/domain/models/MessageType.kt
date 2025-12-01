// 📁 domain/models/MessageType.kt
package com.konchak.cnc_halper.domain.models

enum class MessageType {
    USER_MESSAGE,      // Сообщение от пользователя
    AI_RESPONSE,       // Ответ ИИ
    SYSTEM_MESSAGE,    // Системное сообщение
    ERROR,             // Ошибка
    NEED_TRAINING      // ИИ просит обучения
}