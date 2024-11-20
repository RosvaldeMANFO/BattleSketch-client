package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(
    val content: String,
    val sender: PlayerEntity? = null,
    val messageType: MessageType
)