// app/src/main/java/com/konchak/cnc_halper/presentation/MainActivity.kt
package com.konchak.cnc_halper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.konchak.cnc_halper.core.theme.CNCTheme
import com.konchak.cnc_halper.presentation.navigation.Screen
import com.konchak.cnc_halper.presentation.navigation.appGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CNCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route
                    ) {
                        appGraph(navController)
                    }
                }
            }
        }
    }
}