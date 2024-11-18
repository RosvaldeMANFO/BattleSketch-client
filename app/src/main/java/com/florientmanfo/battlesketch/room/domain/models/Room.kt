package com.florientmanfo.battlesketch.room.domain.models

import com.florientmanfo.battlesketch.core.domain.models.Player

data class Room(
    val name: String = "",
    val creator: String = "",
    val password: String? = null,
    val players: List<Player> = mutableListOf(),
)
