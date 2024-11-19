package com.florientmanfo.battlesketch.core.domain.models

import androidx.compose.runtime.mutableStateListOf

data class Player(
    val name: String,
    val score: Int,
    val isCurrentPlayer: Boolean,
)

val players = mutableStateListOf(
    Player(
        "Paul",
        0,
        true
    ),
    Player(
        "Jean",
        0,
        false
    ),
    Player(
        "Henry",
        0,
        false
    ),
    Player(
        "Leonard",
        0,
        false
    )
)