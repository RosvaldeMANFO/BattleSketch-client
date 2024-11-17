package com.florientmanfo.battlesketch.board.data

import com.florientmanfo.battlesketch.board.data.entities.SocketResponseEntity
import com.florientmanfo.battlesketch.core.data.KtorClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.takeFrom
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

class WebSocketDataSource {
    private lateinit var socket: DefaultClientWebSocketSession

    suspend fun connect(): Flow<Result<SocketResponseEntity>> {
        socket = KtorClient.httpClient.webSocketSession {
            url.takeFrom("ws://10.0.2.2:8080/")
        }
        return socket.incoming
            .receiveAsFlow()
            .map { frame ->
                val textFrame = frame as Frame.Text
                val data = Json.decodeFromString<SocketResponseEntity>(textFrame.readText())
                Result.success(data)
            }
            .catch { error ->
                when (error) {
                    is ClosedReceiveChannelException -> emit(
                        Result.failure(Error())
                    )

                    else -> emit(
                        Result.failure(Error(SocketError.UnexpectedError.message))
                    )
                }
            }
    }

    suspend fun close() {
        socket.close(CloseReason(CloseReason.Codes.NORMAL, SocketError.SessionClosed.message))
    }
}