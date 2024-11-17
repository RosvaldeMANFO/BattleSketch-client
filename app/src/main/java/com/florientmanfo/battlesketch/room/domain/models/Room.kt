package com.florientmanfo.battlesketch.room.domain.models

data class Room(
    val name: String = "",
    val creator: String = "",
    val password: String? = null,
    val players: List<Player> = mutableListOf(),
)
