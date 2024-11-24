package com.florientmanfo.battlesketch.room.domain.use_cases

import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class WatchRoomListUseCase(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(): Flow<Message>{
        return repository.watchRoomList()
    }
}