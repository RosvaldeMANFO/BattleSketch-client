package com.florientmanfo.battlesketch.board.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.board.presentation.components.Board
import com.florientmanfo.battlesketch.board.presentation.components.HoldingDialog
import com.florientmanfo.battlesketch.board.presentation.components.TimeoutDialog
import com.florientmanfo.battlesketch.board.presentation.components.WinnerDialog
import com.florientmanfo.battlesketch.core.presentation.components.CustomAlertDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun BoardScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = koinViewModel()
) {
    val state by viewModel.boardState.collectAsStateWithLifecycle()

    BackHandler {
        if (state.payerName == state.sessionData?.roomCreator) {
            viewModel.onUiEvent(BoardUiEvent.OnTriggerRoomClosing)
        } else {
            viewModel.onUiEvent(BoardUiEvent.OnPlayerLeftRoom)
        }
    }

    Box(modifier) {
        state.sessionData?.elapsedTime?.let {
            LinearProgressIndicator(
                progress = { viewModel.computeTimeTick(it) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.TopCenter),
            )
        }
        if (state.showCloseRoomDialog) {
            CustomAlertDialog(
                title = stringResource(R.string.close_room_dialog_title),
                onConfirmRequest = { viewModel.onUiEvent(BoardUiEvent.OnPlayerLeftRoom) },
                onDismissRequest = { viewModel.onUiEvent(BoardUiEvent.OnCancelRoomClosing) }
            ) {
                Text(
                    text = stringResource(R.string.close_room_dialog_message),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        state.sessionData?.let {
            if (it.isRunning && it.wordToGuess.isNotEmpty()) {
                state.winner?.let { winner ->
                    WinnerDialog(
                        wordToGuest = it.wordToGuess,
                        winnerName = winner
                    )
                }
                if (state.timeout) {
                    TimeoutDialog(wordToGuest = it.wordToGuess)
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
                    onQuitRoom = {
                        if (state.payerName == state.sessionData?.roomCreator) {
                            viewModel.onUiEvent(BoardUiEvent.OnTriggerRoomClosing)
                        } else {
                            viewModel.onUiEvent(BoardUiEvent.OnPlayerLeftRoom)
                        }
                    },
                    onGameStart = { wordToGuest ->
                        viewModel.onUiEvent(BoardUiEvent.StartGame(wordToGuest))
                    }
                )
            }
        }
    }
}