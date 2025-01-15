package com.florientmanfo.battlesketch.board.domain.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.core.domain.models.Player

data class SessionData(
    val roomCreator: String,
    val roomName: String,
    var currentPlayer: Player,
    var wordToGuess: String = "",
    var isRunning: Boolean = false,
    val players: MutableList<Player> = mutableListOf(),
    val messages: MutableList<Message> = mutableListOf(),
    val pathData: SnapshotStateList<PathSettings> = mutableStateListOf(),
    val elapsedTime: Int? = null
)