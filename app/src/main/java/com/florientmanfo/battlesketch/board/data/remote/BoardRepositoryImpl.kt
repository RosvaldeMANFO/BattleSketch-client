package com.florientmanfo.battlesketch.board.data.remote

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.florientmanfo.battlesketch.board.domain.models.Message
import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.board.domain.models.SessionData
import com.florientmanfo.battlesketch.board.domain.models.SocketResponse
import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository
import com.florientmanfo.battlesketch.core.data.KtorClient
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import com.florientmanfo.battlesketch.core.domain.models.Player
import com.florientmanfo.battlesketch.core.domain.models.players
import io.ktor.client.request.get
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class BoardRepositoryImpl(
    private val dataSource: BoardDataSource
) : BoardRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun joinRoom(playerName: String, roomName: String, password: String?): Flow<SocketResponse> =
        flow {
            dataSource.connect(playerName, roomName, password).mapLatest {
                it.fold(
                    onFailure = { error ->
                        Log.d("SOCKET_ERROR", "${error.message}")
                    },
                    onSuccess = { result ->
                        val response = SocketResponse(
                            message = Message(
                                content = result.message?.content ?: "",
                                sender = if (result.message?.sender != null) {
                                    Player(
                                        name = result.message.sender.name,
                                        score = result.message.sender.score,
                                    )
                                } else null,
                                messageType = result.message?.messageType
                                    ?: MessageType.UnexpectedError
                            ),
                            pathSettings = if (result.drawingData != null) {
                                PathSettings(
                                    points = result.drawingData.points.map { point ->
                                        Offset(point.x, point.y)
                                    }.toMutableList(),
                                    color = Color(result.drawingData.brush),
                                    strokeWidth = result.drawingData.thickness,
                                    drawingMode = result.drawingData.mode
                                )
                            } else null,
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
                    roomName = data.roomName,
                    wordToGuess = data.wordToGuess,
                    currentPlayer = Player(
                        name = data.currentPlayer.name,
                        score = data.currentPlayer.score,
                    ),
                    messages = data.messages.map {
                        Message(
                            sender = if (it.sender != null)
                                Player(it.sender.name, it.sender.score) else null,
                            content = it.content,
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
                    }.toMutableList(),
                    players = data.players.map {
                        Player(
                            name = it.name,
                            score = it.score
                        )
                    }.toMutableList()
                )
            }
        } else return null
    }
}