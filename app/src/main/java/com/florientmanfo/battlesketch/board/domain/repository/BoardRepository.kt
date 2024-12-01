package com.florientmanfo.battlesketch.board.domain.repository

import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.board.domain.models.SessionData
import com.florientmanfo.battlesketch.core.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    suspend fun joinRoom(playerName: String, roomName: String, password: String? = null): Flow<Message>
    suspend fun getSessionData(roomName: String): SessionData?
    suspend fun sendMessage(message: Message)
    suspend fun sendDrawnData(pathSettings: PathSettings)
    suspend fun closeSocket()
}