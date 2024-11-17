package com.florientmanfo.battlesketch.room.data

import com.florientmanfo.battlesketch.room.data.entities.RoomEntity
import com.florientmanfo.battlesketch.room.domain.models.Player
import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
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

    override suspend fun getAllRoom(): Flow<List<Room>> {
        return dataSource.getAllRoom().mapLatest {
            if (it.isSuccess) {
                mapRooms(it)
            } else {
                emptyList()
            }
        }
    }

    override suspend fun getRoomByName(name: String): Flow<List<Room>> {
        return dataSource.getRoomByName(name).mapLatest {
            if (it.isSuccess) {
                mapRooms(it)
            } else {
                emptyList()
            }
        }
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