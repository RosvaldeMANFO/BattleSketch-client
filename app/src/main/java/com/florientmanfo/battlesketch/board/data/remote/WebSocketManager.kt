package com.florientmanfo.battlesketch.board.data.remote

import com.florientmanfo.battlesketch.board.data.entities.PathDataEntity
import com.florientmanfo.battlesketch.board.data.entities.SocketResponseEntity
import com.florientmanfo.battlesketch.core.data.KtorClient
import com.florientmanfo.battlesketch.core.data.entity.MessageEntity
import com.florientmanfo.battlesketch.core.data.entity.PlayerEntity
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.takeFrom
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object WebSocketManager {

    private var socket: DefaultClientWebSocketSession? = null

    private suspend fun connect() {
        if (socket == null) {
            socket = KtorClient.httpClient.webSocketSession {
                url.takeFrom("ws://10.0.2.2:8080/join_room")
            }
        }
    }

    suspend fun watchIncomingData(playerName: String, roomName: String, password: String?):
            Flow<Result<MessageEntity>> = withContext(Dispatchers.IO) {
        connect()
        val player = PlayerEntity(
            name = playerName,
            roomName = roomName,
            password = password
        )

        socket?.sendSerialized(player)

        socket?.incoming
            ?.receiveAsFlow()
            ?.map { frame ->
                if (frame is Frame.Text) {
                    val data = Json.decodeFromString<MessageEntity>(frame.readText())
                    Result.success(data)
                } else throw Error(SocketError.UnexpectedError.message)
            }
            ?.catch { error ->
                when (error) {
                    is ClosedReceiveChannelException -> emit(Result.failure(Error()))

                    else -> emit(Result.failure(Error(SocketError.UnexpectedError.message)))
                }
            } ?: flowOf(Result.failure(Error("Failed to initialize WebSocket")))
    }

    suspend fun sendData(
        messageEntity: MessageEntity? = null,
        drawingDataEntity: PathDataEntity? = null
    ) {
        connect()
        val responseModel = SocketResponseEntity(
            message = messageEntity,
            drawingData = drawingDataEntity
        )
        socket?.sendSerialized(responseModel)
    }

    suspend fun close() {
        socket?.sendSerialized(
            SocketResponseEntity(
                message = MessageEntity(MessageType.PlayerLeft),
                drawingData = null
            )
        )
        socket?.close(CloseReason(CloseReason.Codes.NORMAL, SocketError.SessionClosed.message))
        socket = null
    }
}