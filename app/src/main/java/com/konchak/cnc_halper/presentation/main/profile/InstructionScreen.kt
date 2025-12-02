package com.konchak.cnc_halper.presentation.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Инструкция") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = """
                    Добро пожаловать в CNC Helper!

                    1.  **Чат с ИИ:** Задавайте вопросы в чате, и ИИ постарается вам помочь. Если он не знает ответа, вы можете обучить его в разделе 'Управление знаниями'.
                    2.  **Управление знаниями:** В профиле вы можете просматривать, добавлять, редактировать и удалять знания ИИ.
                    3.  **Экспорт знаний:** В профиле вы можете экспортировать базу знаний в JSON-файл.
                    4.  **Калькулятор в чате:** Вы можете использовать чат для расчета скорости резания и подачи.
                        -   Пример запроса для скорости резания: 'рассчитать скорость резания для диаметра 12 и обороты 4000'
                        -   Пример запроса для подачи: 'посчитай подачу для обороты 2000, подача на зуб 0.15, количество зубьев 2'
                """.trimIndent()
            )
        }
    }
}
