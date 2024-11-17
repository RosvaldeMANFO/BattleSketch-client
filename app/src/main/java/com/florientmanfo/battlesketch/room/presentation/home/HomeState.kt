package com.florientmanfo.battlesketch.room.presentation.home

import com.florientmanfo.battlesketch.room.domain.models.Room

data class HomeState(
    val showDialog: Boolean = false,
    val room: Room = Room(),
    val errorMessage: String? = null
)