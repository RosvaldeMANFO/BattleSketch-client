package com.florientmanfo.battlesketch.board.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.core.domain.models.Player

@Composable
fun PlayerList(
    modifier: Modifier = Modifier,
    players: MutableList<Player> = mutableStateListOf(),
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(players){index, player ->
            if(index != players.size - 1){
                Column {
                    PlayerCard(player)
                    HorizontalDivider()
                }
            } else {
                PlayerCard(player)
            }
        }
    }
}