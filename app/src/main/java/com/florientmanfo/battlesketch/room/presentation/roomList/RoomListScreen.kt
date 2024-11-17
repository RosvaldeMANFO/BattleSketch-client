package com.florientmanfo.battlesketch.room.presentation.roomList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomList(
    modifier: Modifier = Modifier,
    viewModel: RoomListViewModel = koinViewModel()
) {
    val state by viewModel.roomListState.collectAsState()

    state.selectedRoom?.let {
        JoinRoomDialog(state, viewModel)
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val interactionSource = remember { MutableInteractionSource() }

            BasicTextField(
                modifier = Modifier.fillMaxWidth(0.75f),
                value = state.searchingKeyword,
                cursorBrush = SolidColor(TextFieldDefaults.colors().cursorColor),
                onValueChange = {
                    viewModel.onUiEvent(
                        RoomListUiEvent.OnSearchingKeywordChange(it)
                    )
                },
                textStyle = LocalTextStyle.current.copy(MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        value = state.searchingKeyword,
                        innerTextField = innerTextField,
                        enabled = true,
                        contentPadding = PaddingValues(8.dp),
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        leadingIcon = {
                            Icon(Icons.Default.Search, null)
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.onUiEvent(RoomListUiEvent.RefreshList)
                            }
                            ) { Icon(Icons.Default.Refresh, null) }
                        },
                        shape = RoundedCornerShape(LocalAppDimens.current.size)
                    )
                },
            )
        }
        item {
            Spacer(modifier = Modifier.height(LocalAppDimens.current.margin))
        }

        if (state.searchingKeyword.isNotEmpty()) {
            itemsIndexed(state.filteredRoom) { index, room ->
                RoomListItem(room) {
                    viewModel.onUiEvent(RoomListUiEvent.OnSelectRoom(room))
                }
                if (index != state.filteredRoom.size - 1) {
                    HorizontalDivider()
                }
            }
        } else {
            itemsIndexed(state.allRoom) { index, room ->
                RoomListItem(room) {
                    viewModel.onUiEvent(RoomListUiEvent.OnSelectRoom(room))
                }
                if (index != state.allRoom.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun RoomListItem(
    room: Room,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        headlineContent = {
            Text(
                pluralStringResource(
                    R.plurals.room_player_number_label,
                    room.players.size,
                    room.players.size
                ),
                style = MaterialTheme.typography.labelMedium
            )

        },
        overlineContent = {
            Text(
                room.name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        leadingContent = {
            Icon(
                Icons.Default.People,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = if (room.password != null) {
            { Icon(Icons.Default.Lock, null) }
        } else null
    )
}

@Composable
@Preview
fun RoomListItemPreview() {
    BattleSketchTheme {
        RoomListItem(
            Room(name = "Test", password = "")
        ) { }
    }
}