package com.florientmanfo.battlesketch.board.di

import com.florientmanfo.battlesketch.board.data.remote.BoardDataSource
import com.florientmanfo.battlesketch.board.data.remote.BoardRepositoryImpl
import com.florientmanfo.battlesketch.board.domain.repository.BoardRepository
import com.florientmanfo.battlesketch.board.domain.use_cases.GetSessionDataUseCase
import com.florientmanfo.battlesketch.board.domain.use_cases.JoinRoomUseCase
import com.florientmanfo.battlesketch.board.domain.use_cases.SendMessageUseCase
import com.florientmanfo.battlesketch.board.presentation.BoardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val boardModule = module {
    single { BoardDataSource(get()) }
    single<BoardRepository> { BoardRepositoryImpl(get()) }
    single { JoinRoomUseCase(get()) }
    single { GetSessionDataUseCase(get()) }
    single { SendMessageUseCase(get()) }
    viewModel { BoardViewModel(get(), get(), get(), get(), get()) }
}