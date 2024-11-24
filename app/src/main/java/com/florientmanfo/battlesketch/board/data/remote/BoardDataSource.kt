package com.florientmanfo.battlesketch.board.data.remote

import android.util.Log
import com.florientmanfo.battlesketch.board.data.entities.SessionDataEntity
import com.florientmanfo.battlesketch.core.data.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class BoardDataSource(
    private val ktorClient: KtorClient
) {

    suspend fun getSessionData(roomName: String): Result<SessionDataEntity> = try {
        val response = ktorClient.httpClient.get(urlString = "session/$roomName")
        val result = response.body<SessionDataEntity>()
        Result.success(result)
    } catch (e: Error) {
        Log.d("SESSION_DATA", "${e.message}")
        Result.failure(e)
    }
}