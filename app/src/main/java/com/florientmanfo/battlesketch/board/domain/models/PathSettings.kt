package com.florientmanfo.battlesketch.board.domain.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.florientmanfo.battlesketch.core.domain.models.DrawingMode

data class PathSettings(
    var points: MutableList<Offset> = mutableStateListOf(),
    val color: Color,
    val colorPickerOffset: Offset? = null,
    val strokeWidth: Float = 30f,
    val drawingMode: DrawingMode = DrawingMode.Draw,
    val isLandScape: Boolean = false
)