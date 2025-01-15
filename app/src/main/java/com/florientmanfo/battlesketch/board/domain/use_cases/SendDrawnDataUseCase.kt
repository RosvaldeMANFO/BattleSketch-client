package com.florientmanfo.battlesketch.board.domain.use_cases

import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository

class SendPathDataUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(pathSettings: PathSettings) {
        repository.sendDrawnData(pathSettings)
    }
}