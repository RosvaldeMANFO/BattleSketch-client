package com.florientmanfo.battlesketch.core.domain.models


data class Message(
    val content: String,
    val messageType: MessageType,
    val sender: Player? = null
)