package com.florientmanfo.battlesketch.domain.room.models

data class Room(
    val name: String = "",
    val creator: String = "",
    val password: String? = null,
    val players: MutableList<Player> = mutableListOf(),
)
