package com.konchak.cnc_halper.presentation.main.profile

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.R // Import R for string resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.machine_settings)) }, // Use string resource
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) // Use string resource
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
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.dark_theme)) // Use string resource
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Language selection
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = when (selectedLanguage) {
                        "ru" -> stringResource(R.string.russian)
                        "en" -> stringResource(R.string.english)
                        else -> stringResource(R.string.select_language)
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.language_selection)) },
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
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.russian)) },
                        onClick = {
                            if (selectedLanguage != "ru") {
                                viewModel.setLanguage("ru")
                                activity?.recreate() // Recreate activity to apply language change
                            }
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.english)) },
                        onClick = {
                            if (selectedLanguage != "en") {
                                viewModel.setLanguage("en")
                                activity?.recreate() // Recreate activity to apply language change
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
