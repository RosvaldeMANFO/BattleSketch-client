package com.florientmanfo.battlesketch.presentation.home

data class HomeState(
    val showDialog: Boolean = false,
    val roomName: String = "",
    val roomPassword: String? = null,
    val error: Boolean = false,
    val errorMessage: String = ""
)