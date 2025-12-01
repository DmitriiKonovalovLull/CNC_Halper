package com.konchak.cnc_halper.data.local.database

import com.konchak.cnc_halper.data.local.database.entities.AIKnowledgeEntity

object PreloadedKnowledge {
    val examples = listOf<AIKnowledgeEntity>(
        // Сюда вы будете вставлять экспортированные данные
        // Пример:
        // AIKnowledgeEntity(question = "какая скорость резания для алюминия", answer = "Рекомендуемая скорость резания для алюминия - 300 м/мин", category = "general")
    )
}
