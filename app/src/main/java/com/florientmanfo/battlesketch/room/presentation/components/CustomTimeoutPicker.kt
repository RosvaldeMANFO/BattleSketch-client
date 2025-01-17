package com.florientmanfo.battlesketch.room.presentation.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimeoutPicker(
    value: Int?,
    availableValue: List<Int>,
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf(value) }
    var isEnabled by remember { mutableStateOf(value != null) }

    LaunchedEffect(selectedValue) {
        onValueChange(selectedValue)
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded && isEnabled,
        onExpandedChange = { if (isEnabled) expanded = it }
    ) {
        TextField(
            value = if (selectedValue != null) "$selectedValue Sec"
            else stringResource(R.string.timeout_placeholder),
            onValueChange = {},
            readOnly = true,
            enabled = isEnabled,
            textStyle = TextStyle.Default.copy(
                textAlign = TextAlign.Center
            ),
            trailingIcon = if (isEnabled) {
                { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            } else null,
            leadingIcon = {
                Switch(
                    checked = isEnabled,
                    onCheckedChange = {
                        isEnabled = it
                        selectedValue = if (it) {
                            availableValue.first()
                        } else {
                            null
                        }
                        expanded = false
                    }
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
        )
        ExposedDropdownMenu(
            expanded = expanded && isEnabled,
            onDismissRequest = { expanded = false }
        ) {
            availableValue.forEach { value ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$value Sec",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        selectedValue = value
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun CustomTimeoutPickerPreview() {
    BattleSketchTheme {
        val availableValue = listOf(15, 30, 45, 60)
        CustomTimeoutPicker(
            value = availableValue.first(),
            availableValue = availableValue,
            onValueChange = {}
        )
    }
}
