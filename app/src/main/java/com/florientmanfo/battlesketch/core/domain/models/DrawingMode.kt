package com.florientmanfo.battlesketch.core.domain.models

import kotlinx.serialization.Serializable


@Serializable
enum class DrawingMode {
    Erase,
    Draw
}