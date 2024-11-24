package com.florientmanfo.battlesketch.core.domain.models


data class Message(
    val content: String,
    val sender: Player? = null,
    val messageType: MessageType
)