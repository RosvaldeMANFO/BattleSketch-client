package com.florientmanfo.battlesketch.domain.room.useCases

import com.florientmanfo.battlesketch.domain.room.models.Room
import com.florientmanfo.battlesketch.domain.room.repository.RoomRepository

class CreateRoomUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(room: Room) {
        repository.createRoom(room)
    }
}