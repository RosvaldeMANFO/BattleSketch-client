package com.florientmanfo.battlesketch.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
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
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(LocalAppDimens.current.margin)
            ) {
                Text(
                    stringResource(R.string.create_room_label),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(LocalAppDimens.current.margin))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = onNameChange,
                    shape = CircleShape,
                    singleLine = true,
                    isError = errorMessage != null,
                    placeholder = { Text(stringResource(R.string.name_placeholder)) }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password ?: "",
                    onValueChange = { onPasswordChange(it) },
                    shape = CircleShape,
                    singleLine = true,
                    visualTransformation = { text ->
                        TransformedText(
                            AnnotatedString("*".repeat(text.text.length)),
                            OffsetMapping.Identity
                        )
                    },
                    placeholder = { Text(stringResource(R.string.name_placeholder)) }
                )
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreate,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.create_label),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
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
        }
    }
}

@Composable
@Preview
fun RoomEditingDialogPreview(){
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