package com.florientmanfo.battlesketch.board.domain.models

import com.florientmanfo.battlesketch.core.domain.models.Message

data class SocketResponse(
    val message: Message?,
    val pathSettings: PathSettings?
)
