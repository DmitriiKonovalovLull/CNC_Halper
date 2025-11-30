package com.konchak.cnc_halper.network.models

data class DeepSeekRequest(
    val model: String = "deepseek-chat",
    val messages: List<Message>,
    val stream: Boolean = false
)

data class Message(
    val role: String,
    val content: String
)

data class DeepSeekResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
