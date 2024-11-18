package com.florientmanfo.battlesketch.board.presentation

import androidx.compose.runtime.mutableStateListOf
import com.florientmanfo.battlesketch.board.domain.models.PathSettings

data class BoardState(
    val drawingDate: MutableList<PathSettings> = mutableStateListOf()
)