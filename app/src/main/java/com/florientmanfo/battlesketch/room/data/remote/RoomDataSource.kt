package com.florientmanfo.battlesketch.room.data.remote

import com.florientmanfo.battlesketch.core.data.KtorClient
import com.florientmanfo.battlesketch.room.data.entities.RoomEntity
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RoomDataSource(private val ktorClient: KtorClient) {

    suspend fun createRoom(room: RoomEntity): Result<Unit> {
        return try {
            ktorClient.httpClient.post(urlString = "room/create") {
                contentType(ContentType.Application.Json)
                setBody(room)
            }
            Result.success(Unit)
        } catch (e: Error) {
            Result.failure(e)
        }
    }

    suspend fun getAllRoom(): Result<List<RoomEntity>> = try {
        val response = ktorClient.httpClient.get(urlString = "room")
        val rooms = response.body<List<RoomEntity>>()
            .filter { it.isOpen }
        Result.success(rooms)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getRoomByName(name: String): Result<List<RoomEntity>> = try {
        val response = ktorClient.httpClient.get(urlString = "room/$name")
        val rooms = response.body<List<RoomEntity>>()
            .filter { it.isOpen }
        Result.success(rooms)
    } catch (e: Error) {
        Result.failure(e)
    }
}