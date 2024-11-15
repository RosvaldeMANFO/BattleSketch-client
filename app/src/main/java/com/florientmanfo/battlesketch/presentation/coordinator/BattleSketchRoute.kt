package com.florientmanfo.battlesketch.presentation.coordinator

import androidx.annotation.StringRes
import com.florientmanfo.battlesketch.R
import kotlinx.serialization.Serializable

sealed interface BattleSketchRoute {

    @get:StringRes
    val title: Int

    @Serializable
    data object Home : BattleSketchRoute {
        override val title: Int
            get() = R.string.app_name
    }

    @Serializable
    data class RoomList(
        val roomName: String?,
        val roomPassword: String?,
        val playerName: String?
    ) : BattleSketchRoute {
        override val title: Int
            get() = R.string.room_list_title
    }

    @Serializable
    data class Holding(
        val playerName: String,
        val roomName: String,
    ) : BattleSketchRoute {
        override val title: Int
            get() = R.string.room_list_title
    }

    @Serializable
    data class Board(
        val playerName: String,
        val roomName: String
    ) : BattleSketchRoute {
        override val title: Int
            get() = R.string.room_list_title
    }
}