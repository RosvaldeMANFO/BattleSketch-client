package com.florientmanfo.battlesketch.room.presentation.home

import androidx.annotation.StringRes
import com.florientmanfo.battlesketch.room.domain.models.Room

data class HomeState(
    val showDialog: Boolean = false,
    val room: Room = Room(),
    @StringRes val errorMessage: Int? = null,
    val timeoutValues: List<Int> = listOf(15, 30, 45, 60)
)