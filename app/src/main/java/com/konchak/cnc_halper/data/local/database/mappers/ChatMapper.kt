package com.konchak.cnc_halper.data.local.database.mappers

import com.konchak.cnc_halper.data.local.database.entities.ChatEntity
import com.konchak.cnc_halper.domain.models.ChatMessage

@Suppress("unused")
object ChatMapper {
    fun toEntity(domain: ChatMessage, operatorId: Long): ChatEntity {
        return ChatEntity(
            id = 0, // id будет сгенерирован базой данных
            operatorId = operatorId,
            date = System.currentTimeMillis(),
            question1 = if (domain.isUser) domain.message else "",
            answer1 = if (!domain.isUser) domain.message else "",
            question2 = "",
            answer2 = "",
            question3 = "",
            answer3 = ""
        )
    }

    fun toDomain(entity: ChatEntity): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        if (entity.question1.isNotEmpty()) {
            messages.add(
                ChatMessage(
                    id = entity.id.toString() + "_q1",
                    message = entity.question1,
                    isUser = true,
                    timestamp = entity.date
                )
            )
        }
        if (entity.answer1.isNotEmpty()) {
            messages.add(
                ChatMessage(
                    id = entity.id.toString() + "_a1",
                    message = entity.answer1,
                    isUser = false,
                    timestamp = entity.date
                )
            )
        }
        if (entity.question2.isNotEmpty()) {
            messages.add(
                ChatMessage(
                    id = entity.id.toString() + "_q2",
                    message = entity.question2,
                    isUser = true,
                    timestamp = entity.date
                )
            )
        }
        if (entity.answer2.isNotEmpty()) {
            messages.add(
                ChatMessage(
                    id = entity.id.toString() + "_a2",
                    message = entity.answer2,
                    isUser = false,
                    timestamp = entity.date
                )
            )
        }
        if (entity.question3.isNotEmpty()) {
            messages.add(
                ChatMessage(
                    id = entity.id.toString() + "_q3",
                    message = entity.question3,
                    isUser = true,
                    timestamp = entity.date
                )
            )
        }
        if (entity.answer3.isNotEmpty()) {
            messages.add(
                ChatMessage(
                    id = entity.id.toString() + "_a3",
                    message = entity.answer3,
                    isUser = false,
                    timestamp = entity.date
                )
            )
        }
        return messages
    }

    fun toDomainList(entityList: List<ChatEntity>): List<ChatMessage> {
        return entityList.flatMap { toDomain(it) }
    }
}
