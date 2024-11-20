package com.florientmanfo.battlesketch.room.presentation.roomList

import androidx.annotation.StringRes
import com.florientmanfo.battlesketch.room.domain.models.Room

data class RoomListState(
    val allRoom: List<Room> = listOf(),
    val filteredRoom: List<Room> = mutableListOf(),
    val searchingKeyword: String = "",
    val roomPassword: String? = null,
    val playerName: String = "",
    val selectedRoom: Room? = null,
    @StringRes val errorMessage: Int? = null
)