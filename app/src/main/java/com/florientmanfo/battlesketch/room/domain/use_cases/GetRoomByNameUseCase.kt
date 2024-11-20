package com.florientmanfo.battlesketch.room.domain.use_cases

import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetRoomByNameUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(name: String): List<Room> {
        return repository.getRoomByName(name)
    }
}