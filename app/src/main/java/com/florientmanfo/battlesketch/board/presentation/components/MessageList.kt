package com.florientmanfo.battlesketch.board.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.board.domain.models.Message
import com.florientmanfo.battlesketch.core.presentation.components.CustomTextField
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: MutableList<Message> = mutableStateListOf(),
    onSendMessage: (String) -> Unit,
    showMessages: Boolean = false,
    headContent: @Composable () -> Unit,
) {

    val listState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val messageListHeight: Dp = if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        (configuration.screenHeightDp * 0.3f).dp else configuration.screenHeightDp.dp

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
        headContent()

        AnimatedVisibility(
            showMessages,
            enter = slideInVertically()+ expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(messageListHeight),
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