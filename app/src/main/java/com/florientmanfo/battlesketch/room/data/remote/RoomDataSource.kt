package com.florientmanfo.battlesketch.room.data.remote

import com.florientmanfo.battlesketch.core.data.KtorClient
import com.florientmanfo.battlesketch.core.domain.models.ErrorType
import com.florientmanfo.battlesketch.room.data.entities.RoomEntity
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class RoomDataSource(private val ktorClient: KtorClient) {

    suspend fun createRoom(room: RoomEntity): Result<Unit> {
        val response = ktorClient.httpClient.post(urlString = "room/create") {
            contentType(ContentType.Application.Json)
            setBody(room)
        }
        return if(response.status == HttpStatusCode.BadRequest) {
            val error = response.body<String>().split(',').first()
            when(error){
                "DUPLICATED ROOM NAME" -> Result.failure(Error(ErrorType.DuplicatedRoomName.name))
                else -> Result.failure(Error(""))
            }
        } else Result.success(Unit)
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