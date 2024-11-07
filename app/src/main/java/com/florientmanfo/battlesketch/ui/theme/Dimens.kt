package com.florientmanfo.battlesketch.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(val size: Dp, val margin: Dp)

val smallDimens = Dimensions(size = 32.dp  ,margin = 16.dp)

val mediumDimens = Dimensions(size = 64.dp ,margin = 32.dp)

val largeDimens = Dimensions(size = 128.dp , margin = 48.dp)