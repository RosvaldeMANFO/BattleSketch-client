package com.florientmanfo.battlesketch.presentation.coordinator

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Coordinator {

    private val _navStack = mutableStateListOf<BattleSketchRoute>()
    private val _currentRoute = MutableStateFlow(BattleSketchRoute.Home)
    val currentRoute = _currentRoute.asStateFlow()

    suspend fun navigateTo(destination: BattleSketchRoute){
        if(!_navStack.contains(destination)){
            _navStack.add(destination)
            _currentRoute.emit(destination)
        }
    }
}

enum class BattleSketchRoute {
    Home,
    RoomList,
    HoldRoom,
    Board,
}