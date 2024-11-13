package com.florientmanfo.battlesketch.domain.room.repository

import com.florientmanfo.battlesketch.domain.room.models.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(room: Room)
    suspend fun getAllRoom(): Flow<List<Room>>
    suspend fun getRoomByName(name: String): Flow<List<Room>>
}