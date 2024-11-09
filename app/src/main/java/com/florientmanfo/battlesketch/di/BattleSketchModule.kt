package com.florientmanfo.battlesketch.di

import com.florientmanfo.battlesketch.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel() }
}