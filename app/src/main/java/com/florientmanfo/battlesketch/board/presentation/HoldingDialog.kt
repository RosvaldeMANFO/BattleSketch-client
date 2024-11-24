package com.florientmanfo.battlesketch.board.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.board.domain.models.SessionData
import com.florientmanfo.battlesketch.core.domain.models.Player
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.isTablet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldingDialog(
    sessionData: SessionData,
    isCurrentPlayer: Boolean = false,
    onGameStart: (String) -> Unit,
    onQuitRoom: () -> Unit
) {
    var worldToGuest by remember {
        mutableStateOf("")
    }
    val rowColor = CardDefaults.cardColors().containerColor

    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(LocalAppDimens.current.margin)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.player_list_title),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(onClick = onQuitRoom) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            null
                        )
                    }
                }
                if (isCurrentPlayer) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = worldToGuest,
                        onValueChange = { worldToGuest = it },
                        isError = worldToGuest.length < 3,
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.word_to_guest_placeholder)) }
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(
                        if (isTablet(LocalContext.current)) 0.75f
                        else 0.45f
                    ),
                ) {
                    itemsIndexed(sessionData.players) { index, player ->
                        if (index != sessionData.players.size - 1) {
                            Column {
                                WaitingPlayerRow(player, rowColor)
                                HorizontalDivider()
                            }
                        } else {
                            WaitingPlayerRow(player, rowColor)
                        }
                    }
                }
                if (isCurrentPlayer) {
                    ElevatedButton(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = {
                            if (worldToGuest.length >= 3) {
                                onGameStart(worldToGuest)
                            }
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            stringResource(R.string.start_game_button_label),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.waiting_message),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun WaitingPlayerRow(player: Player, background: Color) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = background),
        overlineContent = {
            Text(
                player.name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        headlineContent = {
            Text(
                stringResource(R.string.player_score, player.score),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Icon(Icons.Default.Person, null)
        },
        trailingContent = if (player.isCurrentPlayer) {
            {
                Icon(Icons.Default.Edit, null)
            }
        } else null
    )
}