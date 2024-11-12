package com.florientmanfo.battlesketch.presentation.roomList

import androidx.lifecycle.ViewModel
import com.florientmanfo.battlesketch.domain.room.models.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RoomListViewModel: ViewModel() {

    private val _roomListState = MutableStateFlow(RoomListState())
    val roomListState = _roomListState.asStateFlow()

    fun onUiEvent(event: RoomListUiEvent){
        when(event){
            is RoomListUiEvent.OnSearchingKeywordChange -> {
                _roomListState.update {
                    it.copy(searchingKeyword = event.value)
                }
            }
            is RoomListUiEvent.OnSelectRoom -> {
                _roomListState.update {
                    it.copy(selectedRoom = event.room)
                }
            }
            is RoomListUiEvent.OnPasswordChange -> {
                _roomListState.update {
                    it.copy(roomPassword = event.value)
                }
            }
            RoomListUiEvent.RefreshList -> {

                _roomListState.update {
                    it.copy(filteredRoom = mutableListOf())
                }
            }
            RoomListUiEvent.OnDismissPasswordDialog -> {
                _roomListState.update {
                    it.copy(
                        selectedRoom = null,
                        roomPassword = "",
                    )
                }
            }
        }
    }
}

sealed interface RoomListUiEvent{
    data object RefreshList: RoomListUiEvent
    data class OnSearchingKeywordChange(val value: String): RoomListUiEvent
    data class OnSelectRoom(val room: Room): RoomListUiEvent
    data class OnPasswordChange(val value: String): RoomListUiEvent
    data object OnDismissPasswordDialog: RoomListUiEvent
}