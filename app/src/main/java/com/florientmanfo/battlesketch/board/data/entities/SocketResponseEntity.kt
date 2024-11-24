package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.data.entity.MessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class SocketResponseEntity(
    val drawingData: DrawingDataEntity? = null,
    val message: MessageEntity? = null
)