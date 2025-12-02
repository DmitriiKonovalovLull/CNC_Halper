package com.konchak.cnc_halper.presentation

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.konchak.cnc_halper.core.theme.CNCTheme
import com.konchak.cnc_halper.core.utils.LocaleHelper
import com.konchak.cnc_halper.data.local.preferences.ThemePreference
import com.konchak.cnc_halper.presentation.navigation.Screen
import com.konchak.cnc_halper.presentation.navigation.appGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreference: ThemePreference

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by themePreference.isDarkMode.collectAsState(initial = false)
            val currentLocale = LocaleHelper.getPersistedLocale(this)

            val localizedContext = remember(currentLocale) {
                LocaleHelper.setLocale(baseContext, currentLocale)
            }
            val configuration = remember(currentLocale) {
                localizedContext.resources.configuration
            }

            CompositionLocalProvider(LocalConfiguration provides configuration) {
                CNCTheme(useDarkTheme = isDarkMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val permissionsState = rememberMultiplePermissionsState(
                            permissions = listOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )

                        if (permissionsState.allPermissionsGranted) {
                            val navController = rememberNavController()
                            key(currentLocale) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Screen.Welcome.route,
                                    modifier = Modifier,
                                    route = "root_graph"
                                ) {
                                    appGraph(navController)
                                }
                            }
                        } else {
                            PermissionRequestScreen(
                                onGrantClick = { permissionsState.launchMultiplePermissionRequest() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionRequestScreen(onGrantClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔒",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Требуются разрешения",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Для полноценной работы приложения необходим доступ к камере (для сканирования) и хранилищу (для сохранения данных).",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onGrantClick) {
            Text("Предоставить доступ")
        }
    }
}
