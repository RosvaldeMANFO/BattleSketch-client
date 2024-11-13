package com.florientmanfo.battlesketch.data

import com.florientmanfo.battlesketch.data.entities.RoomEntity
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomDataSource(
    private val ktor: KtorClient
) {

    suspend fun createRoom(room: RoomEntity): Result<Unit> {
        return try {
            ktor.httpClient.post(urlString = "room/create") {
                contentType(ContentType.Application.Json)
                setBody(room)
            }
            Result.success(Unit)
        } catch (e: Error) {
            Result.failure(e)
        }
    }

    fun getAllRoom(): Flow<Result<List<RoomEntity>>> = flow {
        try {
            val response = ktor.httpClient.get(urlString = "room")
            val rooms = response.body<List<RoomEntity>>()
                .filter { it.isOpen }
            emit(Result.success(rooms))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getRoomByName(name: String): Flow<Result<List<RoomEntity>>> = flow {
        try {
            val response = ktor.httpClient.get(urlString = "room/$name")
            val rooms = response.body<List<RoomEntity>>()
                .filter { it.isOpen }
            emit(Result.success(rooms))
        } catch (e: Error) {
            emit(Result.failure(e))
        }
    }
}