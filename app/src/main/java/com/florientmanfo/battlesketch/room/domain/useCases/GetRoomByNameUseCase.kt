package com.florientmanfo.battlesketch.room.domain.useCases

import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetRoomByNameUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(name: String): Flow<List<Room>> {
        return repository.getRoomByName(name)
    }
}