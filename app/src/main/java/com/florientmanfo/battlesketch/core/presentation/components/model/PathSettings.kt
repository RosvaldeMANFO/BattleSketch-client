package com.florientmanfo.battlesketch.core.presentation.components.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.florientmanfo.battlesketch.ui.theme.blue

data class PathSettings(
    val start: Offset = Offset.Zero,
    val end: Offset = Offset.Zero,
    val color: Color = blue,
    val thickness: Float = 1f,
    val drawingMode: DrawingMode = DrawingMode.Draw,
)