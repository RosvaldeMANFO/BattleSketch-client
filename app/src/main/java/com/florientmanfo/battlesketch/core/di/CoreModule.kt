package com.florientmanfo.battlesketch.core.di

import com.florientmanfo.battlesketch.core.data.KtorClient
import com.florientmanfo.battlesketch.room.data.remote.WebSocketManager
import org.koin.dsl.module

val coreModule = module{
    single { KtorClient }
    single { WebSocketManager }
}