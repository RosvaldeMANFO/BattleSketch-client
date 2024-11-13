package com.florientmanfo.battlesketch.domain.room.useCases

import com.florientmanfo.battlesketch.domain.room.models.Room
import com.florientmanfo.battlesketch.domain.room.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class GetAllRoomUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(): Flow<List<Room>> {
        return repository.getAllRoom()
    }
}