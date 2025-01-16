package com.florientmanfo.battlesketch.room.presentation.roomList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.coordinator.BattleSketchRoute
import com.florientmanfo.battlesketch.coordinator.Coordinator
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.room.domain.use_cases.StopWatchingRoomListUseCase
import com.florientmanfo.battlesketch.room.domain.use_cases.GetAllRoomUseCase
import com.florientmanfo.battlesketch.room.domain.use_cases.GetRoomByNameUseCase
import com.florientmanfo.battlesketch.room.domain.use_cases.WatchRoomListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coordinator: Coordinator,
    private val getAllRoomUseCase: GetAllRoomUseCase,
    private val getRoomByNameUseCase: GetRoomByNameUseCase,
    private val watchRoomListUseCase: WatchRoomListUseCase,
    private val stopWatchingRoomListUseCase: StopWatchingRoomListUseCase
) : ViewModel() {

    private val _roomListState = MutableStateFlow(RoomListState())
    val roomListState = _roomListState.asStateFlow()

    private var args = savedStateHandle.toRoute<BattleSketchRoute.RoomList>()

    init {
        viewModelScope.launch {
            args.roomName?.let {
                coordinator.navigateTo(
                    BattleSketchRoute.Board(
                        playerName = args.playerName!!,
                        roomName = args.roomName!!,
                    )
                )
            }

            getAllRoomUseCase().let { rooms ->
                _roomListState.update {
                    it.copy(allRoom = rooms, loading = false)
                }
            }

            watchRoomListUseCase().collect { message ->
                when (message.messageType) {
                    MessageType.RoomUpdate -> {
                        getAllRoomUseCase().let { rooms ->
                            _roomListState.update {
                                it.copy(allRoom = rooms)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
        coordinator.setCallBack {
            stopWatchingRoomListUseCase()
            true
        }
    }

    fun onUiEvent(event: RoomListUiEvent) {
        when (event) {
            is RoomListUiEvent.OnSearchingKeywordChange -> {
                _roomListState.update {
                    it.copy(searchingKeyword = event.value, loading = true)
                }
                if (event.value.isNotEmpty()) {
                    viewModelScope.launch {
                        getRoomByNameUseCase(event.value).let { rooms ->
                            _roomListState.update {
                                it.copy(
                                    filteredRoom = rooms,
                                    loading = false
                                )
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
                        errorMessage = if (event.value.isNotEmpty() &&
                            event.value != it.selectedRoom?.password
                        )
                            R.string.invalid_room_password
                        else null
                    )
                }
            }

            is RoomListUiEvent.OnPlayerNameChange -> {
                _roomListState.update {
                    it.copy(
                        playerName = event.value
                    )
                }
            }

            RoomListUiEvent.RefreshList -> {
                _roomListState.value = _roomListState.value.copy(loading = true)
                viewModelScope.launch {
                    getAllRoomUseCase().let { rooms ->
                        _roomListState.update {
                            it.copy(
                                allRoom = rooms,
                                filteredRoom = mutableListOf(),
                                searchingKeyword = "",
                                loading = false
                            )
                        }
                    }
                }
            }

            RoomListUiEvent.OnDismissDialog -> {
                _roomListState.update {
                    it.copy(
                        playerName = "",
                        selectedRoom = null,
                        roomPassword = "",
                    )
                }
            }

            RoomListUiEvent.OnConfirmDialog -> {
                if (
                    _roomListState.value.playerName.isNotEmpty()
                    && _roomListState.value.roomPassword ==
                    _roomListState.value.selectedRoom?.password
                ) {
                    _roomListState.value.selectedRoom?.let {
                        if (it.playerNames.contains(_roomListState.value.playerName)) {
                            _roomListState.value = _roomListState.value
                                .copy(errorMessage = R.string.invalid_player_name)
                            throw Error()
                        } else {
                            viewModelScope.launch {
                                coordinator.navigateTo(
                                    BattleSketchRoute.Board(
                                        playerName = _roomListState.value.playerName,
                                        roomName = it.name,
                                        password = _roomListState.value.roomPassword,
                                    )
                                )
                            }
                        }
                    }
                }
            }

            RoomListUiEvent.OnNavigateBack -> {
                viewModelScope.launch {
                    stopWatchingRoomListUseCase()
                    coordinator.navigateBack()
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
    data class OnPlayerNameChange(val value: String) : RoomListUiEvent
    data object OnConfirmDialog : RoomListUiEvent
    data object OnDismissDialog : RoomListUiEvent
    data object OnNavigateBack : RoomListUiEvent
}