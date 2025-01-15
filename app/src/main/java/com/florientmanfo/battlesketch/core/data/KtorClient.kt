package com.florientmanfo.battlesketch.core.data

import com.florientmanfo.battlesketch.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object KtorClient {
    val httpClient: HttpClient = HttpClient(OkHttp) {
        install(WebSockets) {
            pingInterval = 20000.toDuration(DurationUnit.MICROSECONDS)
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        defaultRequest {
            url(BuildConfig.HTTP_BASE_URL)
        }
        install(DefaultRequest) {
            headers.append("Content-Type", "application/json")
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }
}