package com.florientmanfo.battlesketch.board.domain.use_cases

import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository
import com.florientmanfo.battlesketch.core.domain.models.Message

class SendMessageUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(message: Message){
        repository.startGame(message)
    }
}