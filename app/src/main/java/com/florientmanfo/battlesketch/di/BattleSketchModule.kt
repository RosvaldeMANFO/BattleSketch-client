package com.florientmanfo.battlesketch.di

import com.florientmanfo.battlesketch.presentation.coordinator.Coordinator
import com.florientmanfo.battlesketch.presentation.home.HomeViewModel
import com.florientmanfo.battlesketch.presentation.roomList.RoomListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Coordinator() }
    viewModel { HomeViewModel() }
    viewModel { RoomListViewModel() }
}