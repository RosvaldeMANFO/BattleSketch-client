package com.florientmanfo.battlesketch.board.domain.models

import com.florientmanfo.battlesketch.core.domain.models.Player

data class SessionData(
    val roomName: String,
    var wordToGuess: String,
    var currentPlayer: Player,
    val players: MutableList<Player> = mutableListOf(),
    val messages: MutableList<Message> = mutableListOf(),
    val drawingData: MutableList<PathSettings> = mutableListOf()
)