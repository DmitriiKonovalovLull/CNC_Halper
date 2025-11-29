package com.konchak.cnc_halper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.konchak.cnc_halper.presentation.auth.RegistrationScreen
import com.konchak.cnc_halper.presentation.main.MainScreen
import com.konchak.cnc_halper.presentation.main.chat.ChatScreen
import com.konchak.cnc_halper.presentation.main.machines.AddMachineScreen
import com.konchak.cnc_halper.presentation.main.machines.MachineDetailScreen
import com.konchak.cnc_halper.presentation.main.machines.MachineListScreen
import com.konchak.cnc_halper.presentation.main.machines.MachineListViewModel
import com.konchak.cnc_halper.presentation.main.profile.ProfileScreen
import com.konchak.cnc_halper.presentation.main.profile.SettingsScreen
import com.konchak.cnc_halper.presentation.main.tools.AddEditToolScreen
import com.konchak.cnc_halper.presentation.main.tools.EndOperationScreen
import com.konchak.cnc_halper.presentation.main.tools.ToolDetailScreen
import com.konchak.cnc_halper.presentation.main.tools.ToolListScreen
import com.konchak.cnc_halper.presentation.main.tools.ToolScannerScreen
import com.konchak.cnc_halper.presentation.onboarding.EquipmentSetupScreen
import com.konchak.cnc_halper.presentation.onboarding.RoleSelectionScreen
import com.konchak.cnc_halper.presentation.onboarding.TutorialScreen
import com.konchak.cnc_halper.presentation.onboarding.WelcomeScreen

fun NavGraphBuilder.appGraph(navController: NavHostController) {
    composable(Screen.Welcome.route) {
        WelcomeScreen(navController = navController)
    }
    composable(Screen.RoleSelection.route) {
        RoleSelectionScreen(navController = navController)
    }
    composable(Screen.EquipmentSetup.route) {
        EquipmentSetupScreen(navController = navController)
    }
    composable(Screen.Tutorial.route) {
        TutorialScreen(navController = navController)
    }
    composable(Screen.Main.route) {
        MainScreen()
    }

    // Auth flow
    composable(Screen.Registration.route) {
        RegistrationScreen(navController = navController)
    }

    // Machines flow
    composable(Screen.MachineList.route) {
        val viewModel: MachineListViewModel = hiltViewModel()
        MachineListScreen(navController = navController, viewModel = viewModel)
    }
    composable(
        Screen.MachineDetail.route,
        arguments = listOf(navArgument("machineId") { type = NavType.StringType })
    ) { backStackEntry ->
        MachineDetailScreen(
            navController = navController,
            machineId = backStackEntry.arguments?.getString("machineId")
        )
    }
    composable("add_machine") {
        val viewModel: MachineListViewModel = hiltViewModel()
        AddMachineScreen(
            onBack = { navController.popBackStack() },
            onSave = { newMachine ->
                viewModel.addNewMachine(newMachine)
                navController.popBackStack()
            }
        )
    }

    // Tools flow
    composable(Screen.ToolList.route) {
        ToolListScreen(
            navController = navController
        )
    }
    composable(
        Screen.ToolDetail.route,
        arguments = listOf(navArgument("toolId") { type = NavType.StringType })
    ) { backStackEntry ->
        ToolDetailScreen(
            navController = navController,
            toolId = backStackEntry.arguments?.getString("toolId")
        )
    }
    composable(Screen.ToolScanner.route) {
        ToolScannerScreen(navController = navController)
    }
    composable(Screen.AddTool.route) {
        AddEditToolScreen(
            navController = navController,
            toolId = null // Режим добавления
        )
    }
    composable(
        Screen.EditTool.route,
        arguments = listOf(navArgument("toolId") { type = NavType.StringType })
    ) { backStackEntry ->
        AddEditToolScreen(
            navController = navController,
            toolId = backStackEntry.arguments?.getString("toolId") // Режим редактирования
        )
    }
    composable(
        "end_operation/{toolId}",
        arguments = listOf(navArgument("toolId") { type = NavType.StringType })
    ) { backStackEntry ->
        EndOperationScreen(
            navController = navController,
            toolId = backStackEntry.arguments?.getString("toolId")
        )
    }

    // Other screens
    composable(Screen.Chat.route) {
        ChatScreen()
    }
    composable(Screen.Settings.route) {
        SettingsScreen(navController = navController)
    }
    composable(Screen.Profile.route) {
        ProfileScreen(
            onNavigate = { route -> navController.navigate(route) }
        )
    }
}