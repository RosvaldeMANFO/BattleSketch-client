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
import kotlinx.serialization.json.Json

class WebSocketDataSource {
    private lateinit var socket: DefaultClientWebSocketSession

    suspend fun connect(
        onMessage: (SocketResponseEntity) -> Unit,
        onClose: () -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        runCatching {
            socket = KtorClient.httpClient.webSocketSession {
                url.takeFrom("ws://10.0.2.2:8080/")
            }

            while (true) {
                val frame = socket.incoming.receive() as Frame.Text
                val message = Json.decodeFromString<SocketResponseEntity>(frame.readText())
                onMessage(message)
            }

        }.onFailure { error ->
            when (error) {
                is ClosedReceiveChannelException -> onClose()
                else -> onError(
                    error.localizedMessage ?: SocketError.UnexpectedError.message
                )
            }
        }
    }

    suspend fun close() {
        socket.close(CloseReason(CloseReason.Codes.NORMAL, SocketError.SessionClosed.message))
    }
}