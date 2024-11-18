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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.florientmanfo.battlesketch.board.presentation.BoardScreen
import com.florientmanfo.battlesketch.coordinator.BattleSketchNavGraph
import com.florientmanfo.battlesketch.coordinator.BattleSketchRoute
import com.florientmanfo.battlesketch.coordinator.Coordinator
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.isTablet
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


@OptIn(ExperimentalMaterial3Api::class)
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
            val navController = rememberNavController()
            val currentRoute by coordinator.currentRoute.collectAsState()
            val coroutineCope = rememberCoroutineScope()

            LaunchedEffect(currentRoute) {
                val last = coordinator.lastDestination()
                if (last != null && last == currentRoute) {
                    navController.navigate(currentRoute)
                } else navController.popBackStack()
            }

            BattleSketchTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    stringResource(currentRoute.title),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            navigationIcon = {
                                if (currentRoute != BattleSketchRoute.Home) {
                                    IconButton(
                                        onClick = {
                                            coroutineCope.launch {
                                                coordinator.navigateBack()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Default.ArrowBack,
                                            null
                                        )
                                    }
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) { innerPadding ->
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