package com.florientmanfo.battlesketch.board.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.board.presentation.components.Board
import com.florientmanfo.battlesketch.board.presentation.components.HoldingDialog
import com.florientmanfo.battlesketch.board.presentation.components.WinnerDialog
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
                state.winner?.let { winner ->
                    WinnerDialog(
                        wordToGuest = it.wordToGuess,
                        winnerName = winner
                    )
                }
                Board(
                    sessionData = it,
                    showDrawingTools = it.currentPlayer.name == state.payerName,
                    modifier = modifier,
                    onUndo = {
                        viewModel.onUiEvent(BoardUiEvent.OnUndoPath)
                    },
                    onRedo = { pathSettings ->
                        viewModel.onUiEvent(BoardUiEvent.OnPathDrawn(pathSettings))
                    },
                    onReset = { pathSettings ->
                        viewModel.onUiEvent(BoardUiEvent.OnPathDrawn(pathSettings))
                    },
                    onSendMessage = { suggestion ->
                        viewModel.onUiEvent(BoardUiEvent.OnSendSuggestion(suggestion))
                    },
                    onDrawPath = { pathSettings ->
                        viewModel.onUiEvent(BoardUiEvent.OnPathDrawn(pathSettings))
                    }
                )
            } else {
                HoldingDialog(
                    sessionData = it,
                    isCurrentPlayer = it.currentPlayer.name == state.payerName,
                    onQuitRoom = {},
                    onGameStart = { wordToGuest ->
                        viewModel.onUiEvent(BoardUiEvent.StartGame(wordToGuest))
                    }
                )
            }
        }
    }
}