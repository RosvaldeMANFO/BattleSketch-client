package com.florientmanfo.battlesketch.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class RoomEntity(
    val name: String,
    val creator: String,
    val password: String? = null,
    var isOpen: Boolean = true,
    val players: MutableList<PlayerEntity> = mutableListOf(),
)