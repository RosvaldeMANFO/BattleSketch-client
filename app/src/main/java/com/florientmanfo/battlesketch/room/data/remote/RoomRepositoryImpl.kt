package com.florientmanfo.battlesketch.room.data.remote

import com.florientmanfo.battlesketch.core.domain.models.Player
import com.florientmanfo.battlesketch.room.data.entities.RoomEntity
import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val dataSource: RoomDataSource
) : RoomRepository {

    override suspend fun createRoom(room: Room) {
        dataSource.createRoom(
            RoomEntity(
                name = room.name,
                creator = room.creator,
                password = room.password,
            )
        )
    }

    override suspend fun getAllRoom(): List<Room> {
        val result = dataSource.getAllRoom()
        return if (result.isSuccess) mapRooms(result)
        else emptyList()
    }


    override suspend fun getRoomByName(name: String): List<Room> {
        val result = dataSource.getRoomByName(name)
        return if (result.isSuccess) mapRooms(result)
        else emptyList()

    }

    private fun mapRooms(result: Result<List<RoomEntity>>) =
        result.getOrElse { emptyList() }.map { entity ->
            Room(
                name = entity.name,
                creator = entity.creator,
                players = entity.players
                    .map { player ->
                        Player(
                            name = player.name,
                            score = player.score
                        )
                    },
                password = entity.password
            )
        }

}