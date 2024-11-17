package com.florientmanfo.battlesketch.room.presentation.roomList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.core.presentation.components.CustomAlertDialog
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun JoinRoomDialog(
    state: RoomListState,
    viewModel: RoomListViewModel
) {
    CustomAlertDialog(
        title = state.selectedRoom?.name ?: "",
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(LocalAppDimens.current.margin)
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.playerName,
                    onValueChange = {
                        viewModel.onUiEvent(RoomListUiEvent.OnPlayerNameChange(it))
                    },
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.player_name_placeholder)) }
                )
                state.selectedRoom?.password?.let {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.roomPassword,
                        onValueChange = {
                            viewModel.onUiEvent(RoomListUiEvent.OnPasswordChange(it))
                        },
                        singleLine = true,
                        visualTransformation = { text ->
                            TransformedText(
                                AnnotatedString("*".repeat(text.text.length)),
                                OffsetMapping.Identity
                            )
                        },
                        placeholder = { Text(stringResource(R.string.room_password_placeholder)) }
                    )
                }
                state.errorMessage?.let {
                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

            }
        },
        confirmLabel = stringResource(R.string.join_room_label),
        onConfirmRequest = {

        },
        onDismissRequest = {
            viewModel.onUiEvent(RoomListUiEvent.OnDismissDialog)
        }
    )
}
