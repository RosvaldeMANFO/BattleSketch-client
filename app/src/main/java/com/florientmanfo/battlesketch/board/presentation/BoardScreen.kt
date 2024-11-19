package com.florientmanfo.battlesketch.board.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.board.presentation.components.Board
import org.koin.androidx.compose.koinViewModel

@Composable
fun BoardScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = koinViewModel()
) {

    Box(modifier) {
        Board(
            showDrawingTools = true,
            modifier = modifier,
            onUndo = {},
            onRedo = {},
            onReset = { _ -> },
            onSendMessage = {_ ->},
            onDrawPath =  { _ ->}
        )
    }
}