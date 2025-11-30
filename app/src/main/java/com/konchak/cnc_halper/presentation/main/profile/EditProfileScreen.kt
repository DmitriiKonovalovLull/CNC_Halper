package com.konchak.cnc_halper.presentation.main.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ArrowDropDown // Добавлен импорт для иконки выпадающего списка
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.domain.models.Operator
import com.konchak.cnc_halper.domain.models.UserRole
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val role by viewModel.role.collectAsState()
    val workshop by viewModel.workshop.collectAsState()
    val shift by viewModel.shift.collectAsState()
    val experience by viewModel.experience.collectAsState()

    val shifts = listOf("1", "2", "3") // Changed shift options
    // val roles = UserRole.entries.map { it.russianName } // Removed unused variable

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Редактировать профиль") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveProfile()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Сохранить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = workshop,
                onValueChange = viewModel::onWorkshopChanged,
                label = { Text("Цех") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Выпадающий список для Смены
            var shiftExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = shiftExpanded,
                onExpandedChange = { shiftExpanded = !shiftExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = shift,
                    onValueChange = {}, // Только для отображения, изменение через меню
                    readOnly = true,
                    label = { Text("Смена") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = shiftExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = shiftExpanded,
                    onDismissRequest = { shiftExpanded = false }
                ) {
                    shifts.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                viewModel.onShiftChanged(selectionOption)
                                shiftExpanded = false
                            }
                        )
                    }
                }
            }

            // Выпадающий список для Роли
            var roleExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = roleExpanded,
                onExpandedChange = { roleExpanded = !roleExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = role?.russianName ?: "", // Display the russianName of the selected role
                    onValueChange = {}, // Only for display, change via menu
                    readOnly = true,
                    label = { Text("Роль") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = roleExpanded,
                    onDismissRequest = { roleExpanded = false }
                ) {
                    UserRole.entries.forEach { userRole -> // Iterate through UserRole enum entries
                        DropdownMenuItem(
                            text = { Text(userRole.russianName) },
                            onClick = {
                                viewModel.onRoleChanged(userRole) // Pass the UserRole enum directly
                                roleExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = experience,
                onValueChange = viewModel::onExperienceChanged,
                label = { Text("Опыт (мес.)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
            )
        }
    }
}