package com.florientmanfo.battlesketch.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlertDialog(
    title: String,
    modifier: Modifier = Modifier,
    confirmLabel: String = stringResource(R.string.confirm),
    cancelLabel: String? = stringResource(R.string.cancel),
    onDismissRequest: (() -> Unit)? = null,
    onConfirmRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    @StringRes var  errorMessage by remember { mutableStateOf<Int?>(null) }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest?.invoke() }
    ) {
        Card(modifier) {
            Column(
                modifier = Modifier
                    .padding(LocalAppDimens.current.margin)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
                content()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    cancelLabel?.let {
                        TextButton(
                            onClick = { onDismissRequest?.invoke() },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(cancelLabel)
                        }
                    }
                    Spacer(modifier = Modifier.width(LocalAppDimens.current.margin))
                    TextButton(
                        onClick = {
                            try {
                                onConfirmRequest?.invoke()
                                onDismissRequest?.invoke()
                            } catch (e: Error) {
                                e.message?.let {
                                    errorMessage = it.toIntOrNull()
                                }
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(confirmLabel)
                    }
                }
                errorMessage?.let {
                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun CustomAlertDialogPreview() {
    BattleSketchTheme {
        CustomAlertDialog(
            title = "Test",
            content = { Text("Test", style = MaterialTheme.typography.headlineLarge) },
            onConfirmRequest = {},
            onDismissRequest = {}
        )
    }
}