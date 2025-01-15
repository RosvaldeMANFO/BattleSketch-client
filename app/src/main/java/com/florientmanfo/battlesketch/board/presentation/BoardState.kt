package com.florientmanfo.battlesketch.board.presentation

import com.florientmanfo.battlesketch.board.domain.models.SessionData

data class BoardState(
    val sessionData: SessionData? = null,
    val errorMessage: String? = null,
    val payerName: String = "",
    val winner: String? = null,
    val showCloseRoomDialog: Boolean = false,
    val timeout: Boolean = false
)