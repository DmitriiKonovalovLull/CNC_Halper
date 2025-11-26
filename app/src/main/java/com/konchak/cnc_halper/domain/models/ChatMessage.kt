package com.konchak.cnc_halper.domain.models

data class ChatMessage(
    val id: String,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long,
    val messageType: MessageType = MessageType.TEXT
)