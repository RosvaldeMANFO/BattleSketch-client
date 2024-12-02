package com.florientmanfo.battlesketch.room.presentation.home

import androidx.annotation.StringRes
import com.florientmanfo.battlesketch.room.domain.models.Room

data class HomeState(
    val showDialog: Boolean = false,
    val room: Room = Room(),
    @StringRes val errorMessage: Int? = null
)