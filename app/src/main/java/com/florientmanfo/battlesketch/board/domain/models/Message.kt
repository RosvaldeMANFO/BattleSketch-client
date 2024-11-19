package com.florientmanfo.battlesketch.board.domain.models

import com.florientmanfo.battlesketch.core.domain.models.Player


data class Message(
    val content: String,
    val sender: Player? = null,
    val messageType: MessageType
)

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

val fake = mutableListOf(
    Message(
        "Maison",
        Player("Test", 0, true),
        MessageType.Suggestion
    ),
    Message(
        "Maison",
        Player("Test", 0, false),
        MessageType.Suggestion
    ),
    Message(
        "Maison",
        Player("Test", 0, false),
        MessageType.Suggestion
    ),
    Message(
        "Maison",
        Player("Test", 0, false),
        MessageType.Suggestion
    )
)