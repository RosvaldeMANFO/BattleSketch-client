package com.florientmanfo.battlesketch.board.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.florientmanfo.battlesketch.board.domain.models.Message
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: MutableList<Message> = mutableStateListOf(),
    showMessages: Boolean = false,
    headContent: (@Composable () -> Unit)?,
) {

    val listState = rememberLazyListState()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .padding(LocalAppDimens.current.margin),
        verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        headContent?.invoke()

        AnimatedVisibility(
            showMessages,
            enter = slideInVertically()+ expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxHeight(1f),
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(messages) {
                    MessageCard(
                        message = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}