// app/src/main/java/com/konchak/cnc_halper/data/local/database/entities/ChatEntity.kt
package com.konchak.cnc_halper.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "chat_responses")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "operator_id")
    val operatorId: Long,

    @ColumnInfo(name = "date")
    val date: Long, // YYYY-MM-DD

    @ColumnInfo(name = "question_1")
    val question1: String,

    @ColumnInfo(name = "answer_1")
    val answer1: String,

    @ColumnInfo(name = "question_2")
    val question2: String,

    @ColumnInfo(name = "answer_2")
    val answer2: String,

    @ColumnInfo(name = "question_3")
    val question3: String,

    @ColumnInfo(name = "answer_3")
    val answer3: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)