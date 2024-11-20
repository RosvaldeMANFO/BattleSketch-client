package com.florientmanfo.battlesketch.board.presentation

import com.florientmanfo.battlesketch.board.domain.models.SessionData

data class BoardState(
    val sessionData: SessionData? = null,
    val errorMessage: String? = null,
    val payerName: String = ""
)