package com.florientmanfo.battlesketch.presentation.roomList

import com.florientmanfo.battlesketch.domain.room.models.Room

data class RoomListState(
    val allRoom: List<Room> = listOf(),
    val filteredRoom: List<Room> = mutableListOf(),
    val searchingKeyword: String = "",
    val roomPassword: String = "",
    val selectedRoom: Room? = null,
    val isError: Boolean = false
)