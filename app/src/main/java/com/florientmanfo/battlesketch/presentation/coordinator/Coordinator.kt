package com.florientmanfo.battlesketch.presentation.coordinator

import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateListOf
import com.florientmanfo.battlesketch.R
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

    suspend fun navigateBack(){
       if(_navStack.isNotEmpty()){
           _navStack.removeLast()
           _currentRoute.emit(_navStack.lastOrNull() ?: BattleSketchRoute.Home)
       }
    }
}

enum class BattleSketchRoute(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    RoomList(title = R.string.room_list_title),
    HoldRoom(title = R.string.room_list_title),
    Board(title = R.string.room_list_title),
}