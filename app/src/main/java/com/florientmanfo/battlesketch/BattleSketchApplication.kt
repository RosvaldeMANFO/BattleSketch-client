package com.florientmanfo.battlesketch

import android.app.Application
import com.florientmanfo.battlesketch.board.di.boardModule
import com.florientmanfo.battlesketch.room.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BattleSketchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BattleSketchApplication)
            modules (roomModule, boardModule)
        }
    }
}