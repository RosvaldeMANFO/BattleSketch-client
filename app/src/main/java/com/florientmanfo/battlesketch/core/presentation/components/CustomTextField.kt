package com.florientmanfo.battlesketch.core.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = modifier,
        value = value,
        cursorBrush = SolidColor(TextFieldDefaults.colors().cursorColor),
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(MaterialTheme.colorScheme.onSurface),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = LocalAppDimens.current.margin
                ),
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                shape = RoundedCornerShape(LocalAppDimens.current.size)
            )
        },
    )
}