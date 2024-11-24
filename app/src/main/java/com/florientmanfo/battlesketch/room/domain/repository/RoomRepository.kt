package com.florientmanfo.battlesketch.room.domain.repository

import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.room.domain.models.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(room: Room)
    suspend fun getAllRoom(): List<Room>
    suspend fun getRoomByName(name: String): List<Room>
    suspend fun watchRoomList(): Flow<Message>
}