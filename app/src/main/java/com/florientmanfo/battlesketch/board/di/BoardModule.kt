package com.florientmanfo.battlesketch.board.di

import com.florientmanfo.battlesketch.board.data.remote.WebSocketDataSource
import org.koin.dsl.module

val boardModule = module {
    single { WebSocketDataSource() }
}