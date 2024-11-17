package com.florientmanfo.battlesketch.core.presentation.components.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

data class DefaultColor(val color: Color, val initialSelectionState: Boolean ) {
    var selected by mutableStateOf(initialSelectionState)
}