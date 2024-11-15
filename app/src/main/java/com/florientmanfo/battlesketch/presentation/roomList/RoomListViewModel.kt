package com.florientmanfo.battlesketch.presentation.roomList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.florientmanfo.battlesketch.domain.room.models.Room
import com.florientmanfo.battlesketch.domain.room.useCases.GetAllRoomUseCase
import com.florientmanfo.battlesketch.domain.room.useCases.GetRoomByNameUseCase
import com.florientmanfo.battlesketch.presentation.coordinator.BattleSketchRoute
import com.florientmanfo.battlesketch.presentation.coordinator.Coordinator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coordinator: Coordinator,
    private val getAllRoomUseCase: GetAllRoomUseCase,
    private val getRoomByNameUseCase: GetRoomByNameUseCase
) : ViewModel() {

    private val _roomListState = MutableStateFlow(RoomListState())
    val roomListState = _roomListState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllRoomUseCase().collectLatest { rooms ->
                _roomListState.update {
                    it.copy(allRoom = rooms)
                }
            }
        }
    }

    fun onUiEvent(event: RoomListUiEvent) {
        when (event) {
            is RoomListUiEvent.OnSearchingKeywordChange -> {
                _roomListState.update {
                    it.copy(searchingKeyword = event.value)
                }
                if (event.value.isNotEmpty()) {
                    viewModelScope.launch {
                        getRoomByNameUseCase(event.value).collectLatest { rooms ->
                            _roomListState.update {
                                it.copy(filteredRoom = rooms)
                            }
                        }
                    }
                }
            }

            is RoomListUiEvent.OnSelectRoom -> {
                _roomListState.update {
                    it.copy(selectedRoom = event.room)
                }
            }

            is RoomListUiEvent.OnPasswordChange -> {
                _roomListState.update {
                    it.copy(
                        roomPassword = event.value,
                        isError = event.value.isNotEmpty() &&
                                event.value != it.selectedRoom?.password

                    )
                }
            }

            RoomListUiEvent.RefreshList -> {
                viewModelScope.launch {
                    getAllRoomUseCase().collectLatest { rooms ->
                        _roomListState.update {
                            it.copy(
                                allRoom = rooms,
                                filteredRoom = mutableListOf(),
                                searchingKeyword = "",
                            )
                        }
                    }
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

sealed interface RoomListUiEvent {
    data object RefreshList : RoomListUiEvent
    data class OnSearchingKeywordChange(val value: String) : RoomListUiEvent
    data class OnSelectRoom(val room: Room) : RoomListUiEvent
    data class OnPasswordChange(val value: String) : RoomListUiEvent
    data object OnDismissPasswordDialog : RoomListUiEvent
}