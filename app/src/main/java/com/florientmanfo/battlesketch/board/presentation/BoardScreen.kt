package com.florientmanfo.battlesketch.board.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.board.presentation.components.Board
import com.florientmanfo.battlesketch.board.presentation.components.HoldingDialog
import com.florientmanfo.battlesketch.core.domain.models.Message
import com.florientmanfo.battlesketch.core.domain.models.MessageType
import org.koin.androidx.compose.koinViewModel

@Composable
fun BoardScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = koinViewModel()
) {
    val state by viewModel.boardState.collectAsState()

    Box(modifier) {
        state.sessionData?.let {
            if (it.isRunning && it.wordToGuess.isNotEmpty()) {
                Board(
                    sessionData = it,
                    showDrawingTools = it.currentPlayer.name == state.payerName,
                    modifier = modifier,
                    onUndo = {},
                    onRedo = {},
                    onReset = { _ -> },
                    onSendMessage = { _ -> },
                    onDrawPath = { _ -> }
                )
            } else {
                HoldingDialog(
                    sessionData = it,
                    isCurrentPlayer = it.currentPlayer.name == state.payerName,
                    onQuitRoom = {},
                    onGameStart = {wordToGuest ->
                        viewModel.onUiEvent(BoardUiEvent.StartGame(
                            Message(
                                content = wordToGuest,
                                messageType = MessageType.GameStarted,
                                sender = state.sessionData?.players?.first {
                                    player -> player.name == state.payerName
                                }
                            )
                        ))
                    }
                )
            }
        }
    }
}