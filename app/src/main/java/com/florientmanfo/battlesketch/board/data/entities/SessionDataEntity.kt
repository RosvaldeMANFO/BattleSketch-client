package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class SessionDataEntity(
    val roomName: String,
    var wordToGuess: String,
    var currentPlayer: PlayerEntity,
    val players: List<PlayerEntity> = listOf(),
    val messages: List<MessageEntity> = listOf(),
    val drawingData: List<DrawingDataEntity> = listOf()
)


