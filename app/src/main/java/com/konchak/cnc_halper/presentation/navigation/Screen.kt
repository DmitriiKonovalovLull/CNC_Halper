package com.konchak.cnc_halper.presentation.navigation

sealed class Screen(val route: String) {
    // Onboarding flow
    @Suppress("unused")
    object Welcome : Screen("welcome")
    object RoleSelection : Screen("role_selection")
    object EquipmentSetup : Screen("equipment_setup")
    object Tutorial : Screen("tutorial")

    // Main flow
    object Main : Screen("main")
    object MachineList : Screen("machine_list")
    object MachineDetail : Screen("machine_detail/{machineId}") {
        fun createRoute(machineId: String) = "machine_detail/$machineId"
    }
    object ToolScanner : Screen("tool_scanner")
    @Suppress("unused")
    object OfflineToolAnalysis : Screen("offline_tool_analysis")
    object Chat : Screen("chat")
    @Suppress("unused")
    object OfflineChat : Screen("offline_chat")
    @Suppress("unused")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

