package com.florientmanfo.battlesketch.board.domain.use_cases

import com.florientmanfo.battlesketch.board.domain.models.SessionData
import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository

class GetSessionDataUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(roomName: String): SessionData? {
        return repository.getSessionData(roomName)
    }
}