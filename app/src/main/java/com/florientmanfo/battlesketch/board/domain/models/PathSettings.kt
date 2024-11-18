package com.florientmanfo.battlesketch.board.domain.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PathSettings(
    val points: MutableList<Offset> = mutableStateListOf(),
    val color: Color,
    val colorPickerOffset: Offset? = null,
    val strokeWidth: Float = 1f,
    val drawingMode: DrawingMode = DrawingMode.Draw,
)