package com.florientmanfo.battlesketch.presentation.home


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {

    private val _homeSate = MutableStateFlow(HomeState())
    val homeState = _homeSate.asStateFlow()

    fun onUiEvent(event: UiEvent){
        when(event){
            is UiEvent.OnTypingRoomName -> {
                _homeSate.update {
                    it.copy(roomName = event.roomName)
                }
            }
            is UiEvent.OnTypingRoomPassword -> {
                _homeSate.update {
                    it.copy(roomPassword = event.roomPassword)
                }
            }
            is UiEvent.OnToggleDialog -> {
                _homeSate.update {
                    it.copy(showDialog = event.show)
                }
            }
            UiEvent.OnJoinRoom -> {

            }
            UiEvent.OnSubmitRoom -> {
                throw Error("An unexpected error occurred !")
            }
        }
    }
}

sealed class UiEvent{
    data object OnJoinRoom: UiEvent()
    data class OnToggleDialog(val show: Boolean): UiEvent()
    data class OnTypingRoomName(val roomName: String): UiEvent()
    data class OnTypingRoomPassword(val roomPassword: String): UiEvent()
    data object OnSubmitRoom: UiEvent()
}