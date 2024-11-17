package com.florientmanfo.battlesketch.room.di

import com.florientmanfo.battlesketch.coordinator.Coordinator
import com.florientmanfo.battlesketch.core.data.KtorClient
import com.florientmanfo.battlesketch.room.data.RoomDataSource
import com.florientmanfo.battlesketch.room.data.RoomRepositoryImpl
import com.florientmanfo.battlesketch.room.domain.repository.RoomRepository
import com.florientmanfo.battlesketch.room.domain.useCases.CreateRoomUseCase
import com.florientmanfo.battlesketch.room.domain.useCases.GetAllRoomUseCase
import com.florientmanfo.battlesketch.room.domain.useCases.GetRoomByNameUseCase
import com.florientmanfo.battlesketch.room.presentation.home.HomeViewModel
import com.florientmanfo.battlesketch.room.presentation.roomList.RoomListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomModule = module {
    single { Coordinator() }
    single { RoomDataSource() }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single { GetAllRoomUseCase(get()) }
    single { GetRoomByNameUseCase(get()) }
    single { CreateRoomUseCase(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RoomListViewModel(get(), get(), get(), get()) }
}