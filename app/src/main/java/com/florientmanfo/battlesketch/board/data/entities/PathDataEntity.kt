package com.florientmanfo.battlesketch.board.data.entities

import com.florientmanfo.battlesketch.core.domain.models.DrawingMode
import kotlinx.serialization.Serializable

@Serializable
data class Offset(
    val x: Float,
    val y: Float,
)

@Serializable
data class PathDataEntity(
    val points: List<Offset>,
    val mode: DrawingMode,
    val strokeWidth: Float,
    val brush: Long,
    val isLandScape: Boolean
)