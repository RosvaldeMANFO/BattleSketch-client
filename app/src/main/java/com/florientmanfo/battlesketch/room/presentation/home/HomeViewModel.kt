package com.florientmanfo.battlesketch.room.presentation.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florientmanfo.battlesketch.coordinator.BattleSketchRoute
import com.florientmanfo.battlesketch.coordinator.Coordinator
import com.florientmanfo.battlesketch.room.domain.useCases.CreateRoomUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val coordinator: Coordinator,
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    private val _homeSate = MutableStateFlow(HomeState())
    val homeState = _homeSate.asStateFlow()

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnTypingCreatorName -> {
                _homeSate.update {
                    it.copy(room = it.room.copy(creator = event.creatorName))
                }
            }

            is HomeUiEvent.OnTypingRoomName -> {
                _homeSate.update {
                    it.copy(room = it.room.copy(name = event.roomName))
                }
            }

            is HomeUiEvent.OnTypingRoomPassword -> {
                _homeSate.update {
                    it.copy(room = it.room.copy(password = event.roomPassword))
                }
            }

            is HomeUiEvent.OnToggleDialog -> {
                _homeSate.update {
                    it.copy(showDialog = event.show)
                }
            }

            HomeUiEvent.OnJoinRoom -> {
                viewModelScope.launch {
//                    coordinator.navigateTo(
//                        BattleSketchRoute.RoomList(
//                            null,
//                            null,
//                            null
//                        )
//                    )
                    coordinator.navigateTo(
                        BattleSketchRoute.Board(
                            "",
                            ""
                        )
                    )
                }
            }

            HomeUiEvent.OnSubmitRoom -> {
                viewModelScope.launch {
                    try {
                        createRoomUseCase(_homeSate.value.room)
                        coordinator.navigateTo(
                            BattleSketchRoute
                                .RoomList(
                                    _homeSate.value.room.name,
                                    _homeSate.value.room.password,
                                    _homeSate.value.room.creator
                                )
                        )
                    } catch (e: Error) {
                        _homeSate.update {
                            it.copy(
                                errorMessage = e.message ?: "",
                            )
                        }
                    }
                }
            }

            HomeUiEvent.OnDismissDialog -> {
                _homeSate.update {
                    it.copy(
                        showDialog = false
                    )
                }
            }
        }
    }
}

sealed interface HomeUiEvent {
    data object OnJoinRoom : HomeUiEvent
    data class OnToggleDialog(val show: Boolean) : HomeUiEvent
    data class OnTypingCreatorName(val creatorName: String) : HomeUiEvent
    data class OnTypingRoomName(val roomName: String) : HomeUiEvent
    data class OnTypingRoomPassword(val roomPassword: String) : HomeUiEvent
    data object OnSubmitRoom : HomeUiEvent
    data object OnDismissDialog : HomeUiEvent
}