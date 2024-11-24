package com.florientmanfo.battlesketch.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    Refresh,
    Suggestion,
    GameStarted,
    PlayerLeft,
    NewSketch,
    SketchGuessed,
    EmptyRoom,
    RoomUpdate
}
