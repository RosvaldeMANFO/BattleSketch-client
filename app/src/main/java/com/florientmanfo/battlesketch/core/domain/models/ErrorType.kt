package com.florientmanfo.battlesketch.core.domain.models

enum class ErrorType {
    DuplicatedRoomName
}

fun String?.getErrorType(): ErrorType?{
    return ErrorType.entries.firstOrNull { it.name == this }
}