package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import com.florientmanfo.battlesketch.core.domain.models.DrawingMode
import kotlinx.serialization.Serializable

@Serializable
data class Offset(
    val x: Float,
    val y: Float,
)

@Serializable
data class DrawingDataEntity(
    val points: List<Offset>,
    val mode: DrawingMode,
    val thickness: Float,
    val brush: Long
)