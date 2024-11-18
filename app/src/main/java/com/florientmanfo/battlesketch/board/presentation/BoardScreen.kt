package com.florientmanfo.battlesketch.board.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.board.presentation.components.Board
import org.koin.androidx.compose.koinViewModel

@Composable
fun BoardScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = koinViewModel()
) {
    Board(
        modifier = modifier,
        onUndo = {},
        onRedo = {},
        onReset = { _ -> },
        showChat = { _ -> },
    ) { _ -> }
}