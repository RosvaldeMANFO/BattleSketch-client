package com.florientmanfo.battlesketch.core.data.entity

import com.florientmanfo.battlesketch.core.domain.models.MessageType
import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(
    val messageType: MessageType,
    val content: String? = null,
    val sender: PlayerEntity? = null,
)