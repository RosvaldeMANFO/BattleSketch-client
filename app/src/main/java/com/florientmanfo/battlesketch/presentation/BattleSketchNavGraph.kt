package com.florientmanfo.battlesketch.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.florientmanfo.battlesketch.presentation.home.HomeScreen

enum class BattleSketchScreen {
    Home,
    RoomList,
    HoldRoom,
    Board,
}

@Composable
fun BattleSketchNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = BattleSketchScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = BattleSketchScreen.Home.name) {
            HomeScreen(modifier = Modifier.fillMaxSize())
        }
        composable(route = BattleSketchScreen.RoomList.name) {

        }
        composable(route = BattleSketchScreen.HoldRoom.name) {

        }
        composable(route = BattleSketchScreen.Board.name) {

        }
    }
}