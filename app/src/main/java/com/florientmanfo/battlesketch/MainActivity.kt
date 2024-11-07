package com.florientmanfo.battlesketch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.ui.components.DrawingTools
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BattleSketchTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxWidth()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                        ) {}
                        DrawingTools(modifier = Modifier.padding())
                    }
                }

            }
        }
    }
}