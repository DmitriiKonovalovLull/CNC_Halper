package com.konchak.cnc_halper.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konchak.cnc_halper.R // Import R for string resources
import com.konchak.cnc_halper.domain.models.UserRole
import com.konchak.cnc_halper.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(
    navController: NavController,
    viewModel: RoleSelectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Navigate once role is saved and continued
    LaunchedEffect(state.roleSavedAndContinued) {
        if (state.roleSavedAndContinued) {
            navController.navigate(Screen.EquipmentSetup.route) {
                popUpTo(Screen.RoleSelection.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.role_selection_title)) }, // Use string resource
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
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.who_are_you), // Use string resource
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = stringResource(R.string.role_selection_description), // Use string resource
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RoleCard(
                    title = stringResource(R.string.role_operator_title),
                    description = stringResource(R.string.role_operator_description),
                    icon = Icons.Default.PrecisionManufacturing,
                    isSelected = state.selectedRole == UserRole.OPERATOR,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.OPERATOR)) }
                )

                RoleCard(
                    title = stringResource(R.string.role_engineer_title),
                    description = stringResource(R.string.role_engineer_description),
                    icon = Icons.Default.Engineering,
                    isSelected = state.selectedRole == UserRole.ENGINEER,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.ENGINEER)) }
                )

                RoleCard(
                    title = stringResource(R.string.role_programmer_title),
                    description = stringResource(R.string.role_programmer_description),
                    icon = Icons.Default.Code,
                    isSelected = state.selectedRole == UserRole.PROGRAMMER,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.PROGRAMMER)) }
                )

                RoleCard(
                    title = stringResource(R.string.role_master_title),
                    description = stringResource(R.string.role_master_description),
                    icon = Icons.Default.Build,
                    isSelected = state.selectedRole == UserRole.MASTER,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.MASTER)) }
                )

                RoleCard(
                    title = stringResource(R.string.role_technologist_title),
                    description = stringResource(R.string.role_technologist_description),
                    icon = Icons.Default.Engineering,
                    isSelected = state.selectedRole == UserRole.TECHNOLOGIST,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.TECHNOLOGIST)) }
                )

                RoleCard(
                    title = stringResource(R.string.role_workshop_master_title),
                    description = stringResource(R.string.role_workshop_master_description),
                    icon = Icons.Default.SupervisorAccount,
                    isSelected = state.selectedRole == UserRole.WORKSHOP_MASTER,
                    onClick = { viewModel.onEvent(RoleSelectionEvent.SelectRole(UserRole.WORKSHOP_MASTER)) }
                )
            }

            // Removed the "Continue" button
            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
