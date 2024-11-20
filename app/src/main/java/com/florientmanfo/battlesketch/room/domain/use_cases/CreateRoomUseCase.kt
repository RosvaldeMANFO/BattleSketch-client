package com.florientmanfo.battlesketch.room.domain.use_cases

import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository

class CreateRoomUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(room: Room) {
        repository.createRoom(room)
    }
}