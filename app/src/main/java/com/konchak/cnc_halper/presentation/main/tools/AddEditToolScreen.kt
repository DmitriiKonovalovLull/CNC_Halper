package com.konchak.cnc_halper.presentation.main.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // ✅ ИСПРАВЛЕНО
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konchak.cnc_halper.domain.models.ManufacturerTool
import com.konchak.cnc_halper.domain.models.ToolType
import com.konchak.cnc_halper.presentation.main.tools.components.WearLevelSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditToolScreen(
    navController: NavHostController,
    toolId: String? = null,
    viewModel: AddEditToolViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val title = if (toolId == null) "Добавить инструмент" else "Редактировать инструмент"

    LaunchedEffect(toolId) {
        if (toolId != null) {
            viewModel.loadTool(toolId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") // ✅ ИСПРАВЛЕНО
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (toolId == null) {
                                viewModel.saveTool()
                            } else {
                                viewModel.updateTool(toolId)
                            }
                        },
                        enabled = state.isFormValid
                    ) {
                        Text("💾", style = MaterialTheme.typography.titleMedium)
                    }
                }
            )
        }
    ) { paddingValues ->
        ToolForm(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolForm(
    state: AddEditToolState,
    onEvent: (AddEditToolEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 🔍 ПОИСК ПО БАЗЕ ПРОИЗВОДИТЕЛЕЙ - ПЕРВЫЙ ЭКРАН
        SearchManufacturerSection(
            searchQuery = state.searchQuery,
            searchResults = state.searchResults,
            onSearchChanged = { onEvent(AddEditToolEvent.SearchQueryChanged(it)) },
            onToolSelected = { onEvent(AddEditToolEvent.ManufacturerToolSelected(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        // 📝 ОСНОВНАЯ ИНФОРМАЦИЯ (показывается после выбора или ручного ввода)
        if (state.name.isNotBlank() || state.searchQuery.isEmpty()) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Основная информация", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(AddEditToolEvent.NameChanged(it)) },
                        label = { Text("Название инструмента") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.type.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Тип инструмента") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            ToolType.values().forEach { toolType ->
                                DropdownMenuItem(
                                    text = { Text(toolType.displayName) },
                                    onClick = {
                                        onEvent(AddEditToolEvent.TypeChanged(toolType))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 📏 ПАРАМЕТРЫ
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Параметры", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = state.diameter,
                            onValueChange = { onEvent(AddEditToolEvent.DiameterChanged(it)) },
                            label = { Text("Диаметр, мм") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )

                        OutlinedTextField(
                            value = state.length,
                            onValueChange = { onEvent(AddEditToolEvent.LengthChanged(it)) },
                            label = { Text("Длина, мм") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )
                    }

                    OutlinedTextField(
                        value = state.material,
                        onValueChange = { onEvent(AddEditToolEvent.MaterialChanged(it)) },
                        label = { Text("Материал") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )
                }
            }

            // 🎚️ ИЗНОС
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WearLevelSlider(
                        wearLevel = state.wearLevel,
                        onWearLevelChange = { onEvent(AddEditToolEvent.WearLevelChanged(it)) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchManufacturerSection(
    searchQuery: String,
    searchResults: List<ManufacturerTool>,
    onSearchChanged: (String) -> Unit,
    onToolSelected: (ManufacturerTool) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChanged,
            label = { Text("Поиск по производителям") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            )
        )
        if (searchResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    searchResults.forEach { tool ->
                        Text(
                            text = "${tool.name} (${tool.manufacturer})",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToolSelected(tool) }
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}