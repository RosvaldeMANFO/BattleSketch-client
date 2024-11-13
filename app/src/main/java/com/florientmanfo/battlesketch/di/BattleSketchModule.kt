package com.florientmanfo.battlesketch.di

import com.florientmanfo.battlesketch.data.KtorClient
import com.florientmanfo.battlesketch.data.RoomDataSource
import com.florientmanfo.battlesketch.data.RoomRepositoryImpl
import com.florientmanfo.battlesketch.domain.room.repository.RoomRepository
import com.florientmanfo.battlesketch.domain.room.useCases.CreateRoomUseCase
import com.florientmanfo.battlesketch.domain.room.useCases.GetAllRoomUseCase
import com.florientmanfo.battlesketch.domain.room.useCases.GetRoomByNameUseCase
import com.florientmanfo.battlesketch.presentation.coordinator.Coordinator
import com.florientmanfo.battlesketch.presentation.home.HomeViewModel
import com.florientmanfo.battlesketch.presentation.roomList.RoomListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Coordinator() }
    single { RoomDataSource(KtorClient) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single { GetAllRoomUseCase(get()) }
    single { GetRoomByNameUseCase(get()) }
    single { CreateRoomUseCase(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RoomListViewModel(get(), get(), get()) }
}