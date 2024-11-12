package com.florientmanfo.battlesketch

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.florientmanfo.battlesketch.presentation.coordinator.BattleSketchNavGraph
import com.florientmanfo.battlesketch.presentation.coordinator.Coordinator
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.isTablet
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestedOrientation = if (isTablet(this)) {
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val coordinator: Coordinator by inject()

        setContent {
            BattleSketchTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxWidth()
                ) { innerPadding ->
                    val navController = rememberNavController()
                    val currentRoute by coordinator.currentRoute.collectAsState()
                    LaunchedEffect(currentRoute) {
                        navController.navigate(currentRoute.name)
                    }
                    BattleSketchNavGraph(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .imePadding()
                    )
                }

            }
        }
    }
}