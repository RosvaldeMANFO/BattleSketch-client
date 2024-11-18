package com.florientmanfo.battlesketch.board.di

import com.florientmanfo.battlesketch.board.data.remote.WebSocketDataSource
import com.florientmanfo.battlesketch.board.presentation.BoardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module













































val boardModule = module {
    single { WebSocketDataSource() }
    viewModel { BoardViewModel() }
}