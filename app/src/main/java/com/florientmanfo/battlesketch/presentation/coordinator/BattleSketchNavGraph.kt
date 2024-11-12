package com.florientmanfo.battlesketch.presentation.coordinator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.florientmanfo.battlesketch.presentation.home.HomeScreen
import com.florientmanfo.battlesketch.presentation.roomList.RoomList

@Composable
fun BattleSketchNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = BattleSketchRoute.Home.name,
        modifier = modifier
    ) {
        composable(route = BattleSketchRoute.Home.name) {
            HomeScreen(modifier = Modifier.fillMaxSize())
        }
        composable(route = BattleSketchRoute.RoomList.name) {
            RoomList(modifier = Modifier.fillMaxSize())
        }
        composable(route = BattleSketchRoute.HoldRoom.name) {

        }
        composable(route = BattleSketchRoute.Board.name) {

        }
    }
}