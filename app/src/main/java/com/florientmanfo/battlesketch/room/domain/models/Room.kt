package com.florientmanfo.battlesketch.room.domain.models

import com.florientmanfo.battlesketch.room.presentation.home.HomeUiEvent

data class Room(
    val name: String = "",
    val creator: String = "",
    val password: String? = null,
    val playerNames: List<String> = listOf(),
    val timeout: Int? = null
)
