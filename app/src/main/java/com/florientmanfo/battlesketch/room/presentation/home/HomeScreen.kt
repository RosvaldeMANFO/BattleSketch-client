package com.florientmanfo.battlesketch.room.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.core.presentation.components.CustomAlertDialog
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
    state.errorMessage?.let {
        CustomAlertDialog(
            title = stringResource(R.string.warning_dialog_title),
            cancelLabel = null,
            onConfirmRequest = { viewModel.onUiEvent(HomeUiEvent.OnDismissErrorDialog) },
            confirmLabel = stringResource(R.string.info)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.width(LocalAppDimens.current.margin))
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(LocalAppDimens.current.margin))

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