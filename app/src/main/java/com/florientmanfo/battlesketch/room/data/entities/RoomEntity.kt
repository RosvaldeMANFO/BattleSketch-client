package com.florientmanfo.battlesketch.room.data.entities

import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class RoomEntity(
    val name: String,
    val creator: String,
    val password: String? = null,
    var isOpen: Boolean = true,
    val players: MutableList<PlayerEntity> = mutableListOf(),
    val timeout: Int? = null
)