package com.florientmanfo.battlesketch.room.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.homeState.collectAsState()
    if (state.showDialog) {
        RoomEditingDialog(
            state = state,
            viewModel = viewModel
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(0.75f),
            onClick = { viewModel.onUiEvent(HomeUiEvent.OnToggleDialog(true)) },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                stringResource(R.string.create_room_label),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(LocalAppDimens.current.margin))
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(0.75f),
            onClick = { viewModel.onUiEvent(HomeUiEvent.OnJoinRoom) }
        ) {
            Text(
                stringResource(R.string.join_room_label),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}