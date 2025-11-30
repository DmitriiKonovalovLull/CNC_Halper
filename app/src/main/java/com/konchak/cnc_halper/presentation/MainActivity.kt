package com.konchak.cnc_halper.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.konchak.cnc_halper.core.theme.CNCTheme
import com.konchak.cnc_halper.core.utils.LocaleHelper
import com.konchak.cnc_halper.data.local.preferences.ThemePreference
import com.konchak.cnc_halper.presentation.navigation.Screen
import com.konchak.cnc_halper.presentation.navigation.appGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
            val isDarkMode by themePreference.isDarkMode.collectAsState(initial = false) // Читаем предпочтения темы
            val currentLocale = LocaleHelper.getPersistedLocale(this) // Получаем текущую локаль

            // Получаем обновленный контекст для текущей локали
            val localizedContext = remember(currentLocale) {
                LocaleHelper.setLocale(baseContext, currentLocale)
            }
            val configuration = remember(currentLocale) {
                localizedContext.resources.configuration
            }

            CompositionLocalProvider(LocalConfiguration provides configuration) { // Предоставляем обновленную конфигурацию
                CNCTheme(useDarkTheme = isDarkMode) { // Передаем предпочтение темы в CNCTheme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        key(currentLocale) { // Используем currentLocale как ключ для перекомпоновки
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Welcome.route,
                                modifier = Modifier,
                                route = "root_graph"
                            ) {
                                appGraph(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}