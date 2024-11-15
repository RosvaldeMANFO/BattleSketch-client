package com.florientmanfo.battlesketch.presentation.coordinator

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Coordinator {

    private val _navStack = mutableStateListOf<BattleSketchRoute>(BattleSketchRoute.Home)
    private val _currentRoute = MutableStateFlow<BattleSketchRoute>(BattleSketchRoute.Home)
    val currentRoute = _currentRoute.asStateFlow()

    suspend fun navigateTo(route: BattleSketchRoute) {
        if (!_navStack.contains(route)) {
            _navStack.add(route)
            _currentRoute.emit(route)
        }
    }

    fun lastDestination() = _navStack.lastOrNull()

    suspend fun navigateBack() {
        if (_navStack.isNotEmpty()) {
            _navStack.remove(_navStack.last())
            _currentRoute.emit(_navStack.lastOrNull() ?: BattleSketchRoute.Home)
        }
    }
}