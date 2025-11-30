package com.konchak.cnc_halper.presentation.onboarding

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.R // Import R for string resources
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    // val context = LocalContext.current // Removed unused variable
    val activity = (LocalContext.current as? Activity)

    LaunchedEffect(state.isOnboardingCompleted) {
        if (state.isOnboardingCompleted) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "CNC Icon",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.welcome_title), // Use string resource
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.welcome_description), // Use string resource
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    viewModel.onEvent(WelcomeEvent.StartOnboarding)
                    navController.navigate(Screen.RoleSelection.route)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.start_working), // Use string resource
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate(Screen.Registration.route)
                }
            ) {
                Text(stringResource(R.string.register)) // Use string resource
            }

            TextButton(
                onClick = {
                    viewModel.onEvent(WelcomeEvent.ResetOnboarding)
                }
            ) {
                Text(stringResource(R.string.reset_onboarding)) // Use string resource
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
