package com.florientmanfo.battlesketch.room.domain.repository

import com.florientmanfo.battlesketch.room.domain.models.Room

interface RoomRepository {
    suspend fun createRoom(room: Room)
    suspend fun getAllRoom(): List<Room>
    suspend fun getRoomByName(name: String): List<Room>
}