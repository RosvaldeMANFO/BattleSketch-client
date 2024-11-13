package com.florientmanfo.battlesketch.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class PlayerEntity(
    val name: String,
    val roomName: String,
    val password: String? = null,
    var score: Int = 0
)