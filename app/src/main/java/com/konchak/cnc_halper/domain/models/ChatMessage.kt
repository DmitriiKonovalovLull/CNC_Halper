package com.konchak.cnc_halper.domain.models

data class ChatMessage(
    val id: String,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long,
    val messageType: MessageType = if (isUser) MessageType.USER_MESSAGE else MessageType.TEXT
)