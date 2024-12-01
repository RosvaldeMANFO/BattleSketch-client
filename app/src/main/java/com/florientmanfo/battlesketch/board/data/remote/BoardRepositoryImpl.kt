package com.florientmanfo.battlesketch.board.data.remote

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.florientmanfo.battlesketch.board.data.entities.DrawingDataEntity
import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.board.domain.models.SessionData
import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository
import com.florientmanfo.battlesketch.core.data.entity.MessageEntity
import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import com.florientmanfo.battlesketch.core.domain.models.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.florientmanfo.battlesketch.board.data.entities.Offset as EntityOffset

class BoardRepositoryImpl(
    private val dataSource: BoardDataSource
) : BoardRepository {

    override suspend fun joinRoom(playerName: String, roomName: String, password: String?):
            Flow<Message> = flow {
        WebSocketManager.watchIncomingData(playerName, roomName, password).collect { result ->
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

    override suspend fun getSessionData(roomName: String): SessionData? {
        val response = dataSource.getSessionData(roomName)
        return if (response.isSuccess) {
            val data = response.getOrNull()
            if (data == null) null
            else {
                SessionData(
                    roomCreator = data.roomCreator,
                    roomName = data.roomName,
                    wordToGuess = data.wordToGuess,
                    currentPlayer = Player(
                        name = data.currentPlayer.name,
                        score = data.currentPlayer.score,
                        roomName = data.currentPlayer.roomName,
                        isCurrentPlayer = true
                    ),
                    isRunning = data.isRunning,
                    messages = data.messages.map {
                        Message(
                            sender = if (it.sender != null)
                                Player(
                                    it.sender.name,
                                    it.sender.roomName,
                                    it.sender.score,
                                    false,
                                ) else null,
                            content = it.content ?: "",
                            messageType = it.messageType
                        )
                    }.toMutableList(),
                    drawingData = data.drawingData.map {
                        PathSettings(
                            points = it.points.map { point ->
                                Offset(point.x, point.y)
                            }.toMutableList(),
                            color = Color(it.brush),
                            strokeWidth = it.thickness,
                            drawingMode = it.mode
                        )
                    }.toMutableStateList(),
                    players = data.players.map {
                        Player(
                            name = it.name,
                            score = it.score,
                            roomName = it.roomName,
                            isCurrentPlayer = it.name == data.currentPlayer.name
                        )
                    }.toMutableList()
                )
            }
        } else return null
    }

    override suspend fun sendMessage(message: Message) {
        WebSocketManager.sendData(
            messageEntity = MessageEntity(
                content = message.content,
                messageType = message.messageType,
                sender = if(message.sender != null)
                PlayerEntity(
                    name = message.sender.name,
                    roomName = message.sender.roomName
                ) else null
            )
        )
    }

    override suspend fun sendDrawnData(pathSettings: PathSettings) {
        WebSocketManager.sendData(
            drawingDataEntity = DrawingDataEntity(
                points = pathSettings.points.map { EntityOffset( it.x, it.y) },
                mode = pathSettings.drawingMode,
                thickness = pathSettings.strokeWidth,
                brush = pathSettings.color.toArgb().toLong()
            )
        )
    }

    override suspend fun closeSocket() {
        WebSocketManager.close()
    }
}