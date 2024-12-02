package com.florientmanfo.battlesketch.room.domain.use_cases

import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository

class CloseRoomSocketUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(){
        repository.closeRoomSocket()
    }
}