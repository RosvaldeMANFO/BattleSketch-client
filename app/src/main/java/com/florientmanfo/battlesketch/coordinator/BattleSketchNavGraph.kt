package com.florientmanfo.battlesketch.coordinator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.florientmanfo.battlesketch.board.presentation.BoardScreen
import com.florientmanfo.battlesketch.room.presentation.home.HomeScreen
import com.florientmanfo.battlesketch.room.presentation.roomList.RoomListScreen

@Composable
fun BattleSketchNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = BattleSketchRoute.Home,
        modifier = modifier
    ) {
        composable<BattleSketchRoute.Home> {
            HomeScreen(modifier = Modifier.fillMaxSize())
        }
        composable<BattleSketchRoute.RoomList> {
            RoomListScreen(modifier = Modifier.fillMaxSize())
        }
        composable<BattleSketchRoute.Board> {
            BoardScreen(modifier = Modifier.fillMaxSize())
        }
    }
}