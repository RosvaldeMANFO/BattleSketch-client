package com.florientmanfo.battlesketch.room.data.remote

import android.util.Log
import com.florientmanfo.battlesketch.core.data.entity.MessageEntity
import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import com.florientmanfo.battlesketch.core.domain.models.Player
import com.florientmanfo.battlesketch.room.data.entities.RoomEntity
import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomRepositoryImpl(private val dataSource: RoomDataSource) : RoomRepository {

    override suspend fun createRoom(room: Room) {
        dataSource.createRoom(
            RoomEntity(
                name = room.name,
                creator = room.creator,
                password = room.password,
            )
        )
        WebSocketManager.sendMessage(
            MessageEntity(MessageType.RoomUpdate)
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

    override suspend fun watchRoomList(): Flow<Message> = flow {
        WebSocketManager.watchRoomList().collect { result ->
            result.fold(
                onFailure = { error ->
                    Log.d("SOCKET_ERROR", "${error.message}")
                },
                onSuccess = { entity ->
                    val response = Message(
                        content = entity.content ?: "",
                        sender = entity.sender?.let { sender ->
                            Player(
                                name = sender.name,
                                roomName = sender.roomName,
                                score = sender.score,
                                false
                            )
                        },
                        messageType = entity.messageType
                    )
                    emit(response)
                }
            )
        }
    }

    private fun mapRooms(result: Result<List<RoomEntity>>) =
        result.getOrElse { emptyList() }.map { entity ->
            Room(
                name = entity.name,
                creator = entity.creator,
                playerNames = entity.players.map { it.name },
                password = entity.password
            )
        }

}