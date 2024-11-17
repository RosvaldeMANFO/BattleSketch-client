package com.florientmanfo.battlesketch.room.domain.repository

import com.florientmanfo.battlesketch.room.domain.models.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(room: Room)
    suspend fun getAllRoom(): Flow<List<Room>>
    suspend fun getRoomByName(name: String): Flow<List<Room>>
}