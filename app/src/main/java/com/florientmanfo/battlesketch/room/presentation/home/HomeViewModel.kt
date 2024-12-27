package com.florientmanfo.battlesketch.room.presentation.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.coordinator.BattleSketchRoute
import com.florientmanfo.battlesketch.coordinator.Coordinator
import com.florientmanfo.battlesketch.core.domain.models.ErrorType
import com.florientmanfo.battlesketch.room.domain.use_cases.CreateRoomUseCase
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
                    coordinator.navigateTo(
                        BattleSketchRoute.RoomList(
                            null,
                            null,
                            null
                        )
                    )
                }
            }

            HomeUiEvent.OnSubmitRoom -> {
                viewModelScope.launch {
                    val error =  createRoomUseCase(_homeSate.value.room)
                    if(error != null){
                        _homeSate.update {
                            it.copy(
                                errorMessage = when (error){
                                    ErrorType.DuplicatedRoomName -> R.string.invalid_room_name
                                }
                            )
                        }
                    } else  {
                        coordinator.navigateTo(
                            BattleSketchRoute
                                .Board(
                                    _homeSate.value.room.creator,
                                    _homeSate.value.room.name,
                                    _homeSate.value.room.password,
                                )
                        )
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

            HomeUiEvent.OnDismissErrorDialog -> {
                _homeSate.update {
                    it.copy(errorMessage = null)
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
    data object OnDismissErrorDialog: HomeUiEvent
}