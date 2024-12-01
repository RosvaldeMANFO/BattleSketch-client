package com.florientmanfo.battlesketch.board.domain.use_cases

import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository

class QuitSessionUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(){
        repository.closeSocket()
    }
}