package com.florientmanfo.battlesketch.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    Refresh,
    Suggestion,
    GameStarted,
    PlayerLeft,
    SketchGuessed,
    RoomDestroyed,
    RoomUpdate,
    UndoPath,
    UserQuitGame,
}
