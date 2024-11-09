package com.florientmanfo.battlesketch.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BattleSketchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BattleSketchApplication)
            modules (appModule)
        }
    }
}