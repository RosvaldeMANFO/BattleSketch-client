package com.florientmanfo.battlesketch.presentation.roomList

import com.florientmanfo.battlesketch.domain.room.models.Room

data class RoomListState(
    val allRoom: MutableList<Room> = mutableListOf(),
    val filteredRoom: MutableList<Room> = mutableListOf(),
    val searchingKeyword: String = "",
    val roomPassword: String = "",
    val selectedRoom: Room? = null,
    val errorMessage: String? = null
)