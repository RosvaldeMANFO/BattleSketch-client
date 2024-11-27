package com.florientmanfo.battlesketch.core.domain.models

data class Player(
    val name: String,
    val roomName: String,
    val score: Int,
    val isCurrentPlayer: Boolean,
)