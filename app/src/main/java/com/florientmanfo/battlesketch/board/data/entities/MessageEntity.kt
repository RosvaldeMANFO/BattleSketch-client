package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(
    val content: String,
    val sender: PlayerEntity? = null,
    val messageType: MessageType
)

@Serializable
enum class MessageType{
    Suggestion,
    PlayerJoined,
    GameStarted,
    PlayerLeft,
    NewSketch,
    SketchGuessed,
    NextPlayer,
    EmptyRoom
}
