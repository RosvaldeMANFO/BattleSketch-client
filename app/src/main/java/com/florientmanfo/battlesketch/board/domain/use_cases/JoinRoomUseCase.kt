package com.florientmanfo.battlesketch.board.domain.use_cases

import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository
import com.florientmanfo.battlesketch.core.domain.models.Message
import kotlinx.coroutines.flow.Flow

class JoinRoomUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(playerName: String, roomName: String, password: String?): Flow<Message> {
        return repository.joinRoom(playerName, roomName, password)
    }
}
