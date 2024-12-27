package com.florientmanfo.battlesketch.coordinator

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
    fun setCallBack(onNavigateBAck: suspend () -> Boolean) {
        callBck = onNavigateBAck
    }

    suspend fun navigateBack() {
        callBck?.invoke()?.let { result ->
            if (result && _navStack.isNotEmpty()) {
                _navStack.remove(_navStack.last())
                _currentRoute.emit(_navStack.lastOrNull() ?: BattleSketchRoute.Home)
            }
        }
    }

    companion object {
        var callBck: (suspend () -> Boolean)? = null
    }
}