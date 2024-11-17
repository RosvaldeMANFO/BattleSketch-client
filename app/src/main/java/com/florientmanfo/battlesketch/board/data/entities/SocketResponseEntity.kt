package com.florientmanfo.battlesketch.board.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class SocketResponseEntity(
    val drawingData: DrawingDataEntity? = null,
    val message: MessageEntity? = null
)