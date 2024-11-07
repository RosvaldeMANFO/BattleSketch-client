package com.florientmanfo.battlesketch.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.blue
import com.florientmanfo.battlesketch.ui.theme.green
import com.florientmanfo.battlesketch.ui.theme.orange
import com.florientmanfo.battlesketch.ui.theme.red
import com.florientmanfo.battlesketch.ui.theme.yellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    pickerOffset: Offset = Offset.Zero,
    currentColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    onColorChange: (Color, Offset?) -> Unit,
) {
    var showColoWheel by remember {
        mutableStateOf(false)
    }

    if (showColoWheel) {
        ColorWheelDialog(
            currentColor = currentColor,
            currentOffset = pickerOffset?: Offset.Zero,
            onDismissRequest = { showColoWheel = false},
            onPickColor = onColorChange
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
    ) {
        FlowRow(
            maxItemsInEachRow = 3,
            verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
            horizontalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
        ) {
            DefaultColorOptions.entries.forEach {
                if (it.color == null) {
                    if (isSystemInDarkTheme()) {
                        ColorItem(
                            color = Color.White,
                            isSelected = currentColor == Color.White,
                            onClick = {color -> onColorChange(color, null)}
                        )
                    } else {
                        ColorItem(
                            color = Color.Black,
                            isSelected = currentColor == Color.Black,
                            onClick = {color -> onColorChange(color, null)}
                        )
                    }
                } else {
                    ColorItem(
                        color = it.color,
                        isSelected = currentColor == it.color,
                        onClick = {color -> onColorChange(color, null)}
                    )
                }
            }
        }
        IconButton(
            onClick = { showColoWheel = true },
            modifier = Modifier
                .size(LocalAppDimens.current.size)
                .padding(0.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.color_wheel),
                contentDescription = null,
                tint= Color.Unspecified
            )
        }
    }
}

@Composable
fun ColorItem(
    color: Color,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (Color) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val selected by remember {
        mutableStateOf(isSelected)
    }
    val selectionAccent by remember {
        mutableStateOf(
            if (isSelected) {
                if (isDarkTheme) Color.White
                else Color.Black
            } else color
        )
    }

    Box(
        modifier = modifier
            .size(LocalAppDimens.current.size)
            .graphicsLayer {
                clip = true
                shape = CircleShape
            }
            .drawBehind {
                drawCircle(color)
                if (selected) {
                    drawCircle(
                        selectionAccent,
                        style = Stroke(size.width / 7)
                    )
                }
            }
            .clickable {
                onClick(color)
            }
            .border(
                width = if (selected) 1.2.dp else 0.dp,
                shape = CircleShape,
                color = selectionAccent
            )

    )
}

@Composable
fun ColorWheelDialog(
    currentColor: Color,
    currentOffset: Offset,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onPickColor: (Color, Offset) -> Unit
) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var initColor by remember {
        mutableStateOf(currentColor)
    }
    var initOffset by remember {
        mutableStateOf(currentOffset)
    }

    CustomAlertDialog(
        modifier = modifier.onSizeChanged { boxSize = it },
        title = stringResource(R.string.color_wheel_dialog_title),
        onConfirmRequest = {
            onPickColor(initColor, initOffset)
            onDismissRequest()
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
    Yellow(color = yellow),
    Red(color = red),
    Orange(color = orange),
    Bleu(color = blue),
}

@Composable
@Preview(showBackground = false)
fun ColorPalettePreview() {
    BattleSketchTheme {
        ColorPalette(
            onColorChange = {_, _ ->},
        )
    }
}