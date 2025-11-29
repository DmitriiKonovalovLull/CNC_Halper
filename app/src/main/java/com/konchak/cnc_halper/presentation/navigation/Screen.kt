// app/src/main/java/com/konchak/cnc_halper/presentation/navigation/Screen.kt
package com.konchak.cnc_halper.presentation.navigation

sealed class Screen(val route: String) {
    // Onboarding flow
    object Welcome : Screen("welcome")
    object RoleSelection : Screen("role_selection")
    object EquipmentSetup : Screen("equipment_setup")
    object Tutorial : Screen("tutorial")

    // Main flow
    object Main : Screen("main")

    // Auth flow
    object Registration : Screen("registration")

    // Machines flow
    object MachineList : Screen("machine_list")
    object MachineDetail : Screen("machine_detail/{machineId}")

    // Tools flow
    object ToolList : Screen("tool_list")
    object ToolDetail : Screen("tool_detail/{toolId}")
    object ToolScanner : Screen("tool_scanner")
    object AddTool : Screen("add_tool")
    object EditTool : Screen("edit_tool/{toolId}")

    // Other
    object Chat : Screen("chat")
    object Settings : Screen("settings")
    object Profile : Screen("profile") // ✅ ДОБАВЛЕНО
}