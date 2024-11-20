package com.florientmanfo.battlesketch.core.domain.models

import androidx.compose.runtime.mutableStateListOf

data class Player(
    val name: String,
    val score: Int
)

val players = mutableStateListOf(
    Player(
        "Paul",
        0,
    ),
    Player(
        "Jean",
        0,
    ),
    Player(
        "Henry",
        0,
    ),
    Player(
        "Leonard",
        0,
    )
)