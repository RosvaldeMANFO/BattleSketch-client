package com.florientmanfo.battlesketch.room.presentation.roomList

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.room.domain.models.Room
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import org.koin.androidx.compose.koinViewModel
import com.florientmanfo.battlesketch.core.presentation.components.CustomTextField
import com.florientmanfo.battlesketch.room.presentation.components.JoinRoomDialog

@Composable
fun RoomListScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomListViewModel = koinViewModel()
) {
    val state by viewModel.roomListState.collectAsState()

    state.selectedRoom?.let {
        JoinRoomDialog(state, viewModel)
    }

    BackHandler {
        viewModel.onUiEvent(RoomListUiEvent.OnNavigateBack)
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(0.75f),
                onValueChange = {
                    viewModel.onUiEvent(
                        RoomListUiEvent.OnSearchingKeywordChange(it)
                    )
                },
                value = state.searchingKeyword,
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onUiEvent(RoomListUiEvent.RefreshList)
                    }
                    ) { Icon(Icons.Default.Refresh, null) }
                },
                leadingIcon = {
                    IconButton(onClick = {
                        if (state.searchingKeyword.isNotEmpty()) {
                            viewModel.onUiEvent(
                                RoomListUiEvent.OnSearchingKeywordChange(state.searchingKeyword)
                            )
                        }
                    }) {
                        Icon(Icons.Default.Search, null)
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(LocalAppDimens.current.margin))
        }

        if ((state.filteredRoom.isNotEmpty() || state.allRoom.isNotEmpty()) && !state.loading) {
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
        } else {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (state.loading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = stringResource(R.string.no_room_found),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(
                            painter = painterResource(R.drawable.no_room_found),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
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
                    room.playerNames.size,
                    room.playerNames.size
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