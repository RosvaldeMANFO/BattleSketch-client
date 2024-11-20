package com.florientmanfo.battlesketch.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class MessageType{
    Suggestion,
    PlayerJoined,
    GameStarted,
    PlayerLeft,
    NewSketch,
    SketchGuessed,
    NextPlayer,
    EmptyRoom,
    UnexpectedError
}
