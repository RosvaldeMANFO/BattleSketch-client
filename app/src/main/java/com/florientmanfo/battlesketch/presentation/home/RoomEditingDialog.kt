package com.florientmanfo.battlesketch.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.ui.components.CustomAlertDialog
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun RoomEditingDialog(
    name: String,
    modifier: Modifier = Modifier,
    password: String? = null,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onCreate: () -> Unit,
    errorMessage: String? = null
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
                    value = name,
                    onValueChange = onNameChange,
                    singleLine = true,
                    isError = errorMessage != null,
                    placeholder = { Text(stringResource(R.string.name_placeholder)) }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password ?: "",
                    onValueChange = { onPasswordChange(it) },
                    singleLine = true,
                    visualTransformation = { text ->
                        TransformedText(
                            AnnotatedString("*".repeat(text.text.length)),
                            OffsetMapping.Identity
                        )
                    },
                    placeholder = { Text(stringResource(R.string.password_placeholder)) }
                )
                AnimatedVisibility(
                    visible = errorMessage != null
                ) {
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmLabel = stringResource(R.string.create_label),
        onConfirmRequest = onCreate,
        onDismissRequest = onDismissRequest
    )
}

@Composable
@Preview
fun RoomEditingDialogPreview() {
    BattleSketchTheme {
        RoomEditingDialog(
            name = "",
            onNameChange = {},
            onPasswordChange = {},
            onDismissRequest = {},
            onCreate = {}
        )
    }
}