package com.florientmanfo.battlesketch.board.data

enum class SocketError(val message: String) {
    UnexpectedError("An unexpected error occurred."),
    SessionClosed("User session closed.")
}