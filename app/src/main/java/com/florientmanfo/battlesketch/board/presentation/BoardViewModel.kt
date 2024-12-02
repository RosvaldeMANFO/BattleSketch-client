package com.florientmanfo.battlesketch.board.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.board.domain.use_cases.GetSessionDataUseCase
import com.florientmanfo.battlesketch.board.domain.use_cases.JoinRoomUseCase
import com.florientmanfo.battlesketch.board.domain.use_cases.QuitSessionUseCase
import com.florientmanfo.battlesketch.board.domain.use_cases.SendDrawnDataUseCase
import com.florientmanfo.battlesketch.board.domain.use_cases.SendMessageUseCase
import com.florientmanfo.battlesketch.coordinator.BattleSketchRoute
import com.florientmanfo.battlesketch.coordinator.Coordinator
import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BoardViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val sendDrawnDataUseCase: SendDrawnDataUseCase,
    private val quitSessionUseCase: QuitSessionUseCase,
    private val coordinator: Coordinator
) : ViewModel() {

    private val _boardState = MutableStateFlow(BoardState())
    val boardState = _boardState.asStateFlow()

    private val args = savedStateHandle.toRoute<BattleSketchRoute.Board>()


    init {
        viewModelScope.launch {
            _boardState.value = _boardState.value.copy(payerName = args.playerName)
        }
        joinRoom()
    }

    private fun joinRoom() {
        viewModelScope.launch {
            joinRoomUseCase(
                args.playerName,
                args.roomName,
                args.password
            ).catch { e ->
                // Handle any errors here
                Log.d("SOCKET_ERROR", e.message ?: "Unknown error")
            }.collect { message ->
                when (message.messageType) {
                    MessageType.Refresh -> {
                        onRefresh()
                    }

                    MessageType.SketchGuessed -> {
                        _boardState.update {
                            it.copy(winner = message.content)
                        }
                    }

                    MessageType.RoomDestroyed -> {
                        quitSessionUseCase()
                        coordinator.navigateBack()
                    }

                    else -> {
                        quitSessionUseCase()
                        coordinator.navigateBack()
                    }
                }
            }
        }
    }

    fun onUiEvent(event: BoardUiEvent) {
        when (event) {
            is BoardUiEvent.StartGame -> {
                viewModelScope.launch {
                    sendMessageUseCase(
                        Message(
                            content = event.wordToGuest,
                            messageType = MessageType.GameStarted,
                        )
                    )
                }
            }

            is BoardUiEvent.OnPathDrawn -> {
                viewModelScope.launch {
                    sendDrawnDataUseCase(
                        event.pathSettings
                    )
                }
            }

            is BoardUiEvent.OnSendSuggestion -> {
                viewModelScope.launch {
                    sendMessageUseCase(
                        Message(
                            content = event.suggestion,
                            messageType = MessageType.Suggestion,
                        )
                    )
                }
            }

            BoardUiEvent.OnUndoPath -> {
                viewModelScope.launch {
                    sendMessageUseCase(
                        Message(
                            content = "",
                            messageType = MessageType.UndoPath,
                        )
                    )
                }
            }

            BoardUiEvent.OnPlayerLeftRoom -> {
                viewModelScope.launch {
                    sendMessageUseCase(
                        Message(
                            content = "",
                            messageType = MessageType.PlayerLeft,
                        )
                    )
                    quitSessionUseCase()
                    coordinator.navigateBack()
                }
            }

            BoardUiEvent.OnTriggerRoomClosing -> {
                viewModelScope.launch {
                    _boardState.update {
                        it.copy(showCloseRoomDialog = true)
                    }
                }
            }

            BoardUiEvent.OnCancelRoomClosing -> {
                viewModelScope.launch {
                    _boardState.update {
                        it.copy(showCloseRoomDialog = false)
                    }
                }
            }
        }
    }


    private suspend fun onRefresh() {
        val sessionData = getSessionDataUseCase(args.roomName)
        _boardState.update { state ->
            state.copy(
                payerName = args.playerName,
                sessionData = sessionData,
                winner = null
            )
        }
    }
}

sealed interface BoardUiEvent {
    data class StartGame(val wordToGuest: String) : BoardUiEvent
    data class OnPathDrawn(val pathSettings: PathSettings) : BoardUiEvent
    data class OnSendSuggestion(val suggestion: String) : BoardUiEvent
    data object OnUndoPath: BoardUiEvent
    data object OnPlayerLeftRoom: BoardUiEvent
    data object OnCancelRoomClosing: BoardUiEvent
    data object OnTriggerRoomClosing: BoardUiEvent
}