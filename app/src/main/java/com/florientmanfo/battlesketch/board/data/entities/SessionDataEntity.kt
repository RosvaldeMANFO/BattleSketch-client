package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.data.entity.MessageEntity
import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class SessionDataEntity(
    val roomCreator: String,
    val roomName: String,
    var currentPlayer: PlayerEntity,
    var wordToGuess: String = "",
    var isRunning: Boolean = false,
    val players: List<PlayerEntity> = listOf(),
    val messages: List<MessageEntity> = listOf(),
    val drawingData: List<DrawingDataEntity> = listOf()
)


