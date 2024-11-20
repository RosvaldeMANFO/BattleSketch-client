package com.florientmanfo.battlesketch.board.domain.models

import com.florientmanfo.battlesketch.core.domain.models.MessageType
import com.florientmanfo.battlesketch.core.domain.models.Player


data class Message(
    val content: String,
    val sender: Player? = null,
    val messageType: MessageType
)

val fake = mutableListOf(
    Message(
        "Maison",
        Player("Test", 0),
        MessageType.Suggestion
    ),
    Message(
        "Maison",
        Player("Test", 0),
        MessageType.Suggestion
    ),
    Message(
        "Maison",
        Player("Test", 0),
        MessageType.Suggestion
    ),
    Message(
        "Maison",
        Player("Test", 0),
        MessageType.Suggestion
    )
)