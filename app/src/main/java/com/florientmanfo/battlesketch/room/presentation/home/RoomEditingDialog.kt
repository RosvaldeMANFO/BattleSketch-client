package com.florientmanfo.battlesketch.room.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.core.presentation.components.CustomAlertDialog
import com.florientmanfo.battlesketch.room.presentation.components.CustomTimeoutPicker
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun RoomEditingDialog(
    modifier: Modifier = Modifier,
    state: HomeState,
    viewModel: HomeViewModel,
) {
    CustomAlertDialog(
        modifier = modifier,
        title = stringResource(R.string.create_room_label),
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(LocalAppDimens.current.margin)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.room.creator,
                    onValueChange = { viewModel.onUiEvent(HomeUiEvent.OnTypingCreatorName(it)) },
                    singleLine = true,
                    isError = state.errorMessage != null,
                    placeholder = { Text(stringResource(R.string.player_name_placeholder)) }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.room.name,
                    onValueChange = { viewModel.onUiEvent(HomeUiEvent.OnTypingRoomName(it)) },
                    singleLine = true,
                    isError = state.errorMessage != null,
                    placeholder = { Text(stringResource(R.string.room_name_placeholder)) }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.room.password ?: "",
                    onValueChange = { viewModel.onUiEvent(HomeUiEvent.OnTypingRoomPassword(it)) },
                    singleLine = true,
                    visualTransformation = { text ->
                        TransformedText(
                            AnnotatedString("*".repeat(text.text.length)),
                            OffsetMapping.Identity
                        )
                    },
                    placeholder = { Text(stringResource(R.string.room_password_placeholder)) }
                )
                CustomTimeoutPicker(
                    value = state.room.timeout,
                    modifier = Modifier.fillMaxWidth(),
                    availableValue = state.timeoutValues,
                    onValueChange = {viewModel.onUiEvent(HomeUiEvent.OnSetTimeout(it))},
                )
            }
        },
        confirmLabel = stringResource(R.string.create_label),
        onConfirmRequest = { viewModel.onUiEvent(HomeUiEvent.OnSubmitRoom) },
        onDismissRequest = { viewModel.onUiEvent(HomeUiEvent.OnDismissDialog) }
    )
}