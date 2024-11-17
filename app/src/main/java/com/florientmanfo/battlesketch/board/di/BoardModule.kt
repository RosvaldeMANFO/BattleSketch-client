package com.florientmanfo.battlesketch.board.di

import com.florientmanfo.battlesketch.board.data.WebSocketDataSource
import org.koin.dsl.module

val boardModule = module {
    single { WebSocketDataSource() }
}