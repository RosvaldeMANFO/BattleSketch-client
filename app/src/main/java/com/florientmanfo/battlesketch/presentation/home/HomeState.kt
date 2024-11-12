package com.florientmanfo.battlesketch.presentation.home

import com.florientmanfo.battlesketch.domain.room.models.Room

data class HomeState(
    val showDialog: Boolean = false,
    val room: Room = Room(),
    val error: Boolean = false,
    val errorMessage: String = ""
)