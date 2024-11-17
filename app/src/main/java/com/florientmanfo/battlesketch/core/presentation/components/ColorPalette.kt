package com.florientmanfo.battlesketch.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.core.presentation.components.model.DefaultColor
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.blue
import com.florientmanfo.battlesketch.ui.theme.green
import com.florientmanfo.battlesketch.ui.theme.orange
import com.florientmanfo.battlesketch.ui.theme.red

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    initPickerOffset: Offset = Offset.Zero,
    initColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    onColorChange: (Color, Offset?) -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    var showColorWheel by remember { mutableStateOf(false) }
    var currentColor by remember { mutableStateOf(initColor) }
    var currentPickerOffset by remember { mutableStateOf(initPickerOffset) }

    val defaultColors = remember {
        DefaultColorOptions.entries.map {
            val color = it.color ?: if (isDark) Color.White else Color.Black
            DefaultColor(color, false)
        }.toMutableStateList()
    }

    val setSelection: (Int) -> Unit = { selectedIndex ->
        defaultColors.forEachIndexed { index, color ->
            color.selected = index == selectedIndex
        }
        onColorChange(defaultColors[selectedIndex].color, null)
    }

    LaunchedEffect(Unit) {
        defaultColors.first().selected = true
    }

    if (showColorWheel) {
        ColorWheelDialog(
            currentColor = currentColor,
            currentOffset = currentPickerOffset,
            onDismissRequest = { showColorWheel = false },
            onPickColor = { color, offset ->
                currentPickerOffset = offset
                currentColor = color
                onColorChange(color, offset)
                defaultColors.forEach { it.selected = false }
            }
        )
    }

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 3,
        verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
        horizontalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
    ) {
        defaultColors.forEachIndexed { index, item ->
            ColorItem(
                item = item,
                onClick = {
                    setSelection(index)
                    currentColor = item.color
                }
            )
        }
        CustomIconButton(
            onClick = { showColorWheel = true },
            painter = painterResource(R.drawable.color_wheel),
            accent = if (isDark) Color.Black else Color.White,
            contentDescription = stringResource(R.string.color_wheel_dialog_title),
            isSelected = !defaultColors.any { it.selected }
        )
    }
}

@Composable
fun ColorItem(
    item: DefaultColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val accentColor = if (isDarkTheme) Color.Black else Color.White

    Box(
        modifier = modifier
            .size(LocalAppDimens.current.size)
            .clickable { onClick() }
            .graphicsLayer {
                clip = true
                shape = CircleShape
            }
            .drawBehind {
                drawCircle(item.color)
                if (item.selected) {
                    drawCircle(
                        color = accentColor,
                        radius = size.width / 2,
                        style = Stroke(width = size.width / 5)
                    )
                }
            }
    )
}

@Composable
fun ColorWheelDialog(
    currentColor: Color,
    currentOffset: Offset,
    onDismissRequest: () -> Unit,
    onPickColor: (Color, Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var initColor by remember { mutableStateOf(currentColor) }
    var initOffset by remember { mutableStateOf(currentOffset) }

    CustomAlertDialog(
        modifier = modifier.onSizeChanged { boxSize = it },
        title = stringResource(R.string.color_wheel_dialog_title),
        onConfirmRequest = {
            onPickColor(initColor, initOffset)
        },
        onDismissRequest = onDismissRequest
    ) {
        ColorWheel(
            currentColor = initColor,
            currentOffset = initOffset
        ) { color, offset ->
            initColor = color
            initOffset = offset
        }
    }
}

enum class DefaultColorOptions(val color: Color?) {
    Primary(color = null),
    Green(color = green),
    Orange(color = orange),
    Bleu(color = blue),
    Red(color = red),
}


@Composable
@Preview(showBackground = false)
fun ColorPalettePreview() {
    BattleSketchTheme {
        ColorPalette(
            onColorChange = { _, _ -> },
        )
    }
}