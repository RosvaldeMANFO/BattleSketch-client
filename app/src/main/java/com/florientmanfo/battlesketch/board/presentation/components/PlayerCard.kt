package com.florientmanfo.battlesketch.board.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.core.domain.models.Player

@Composable
fun PlayerCard(
    player: Player,
    modifier: Modifier = Modifier
) {
    val color = ListItemDefaults.containerColor

    ListItem(
        colors = ListItemDefaults.colors(containerColor = color),
        modifier = modifier,
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