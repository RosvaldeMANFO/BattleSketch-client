package com.florientmanfo.battlesketch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    currentColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    onColorChange: (Color) -> Unit,
    onPickColor: () -> Unit
) {
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
                            onClick = onColorChange
                        )
                    } else {
                        ColorItem(
                            color = Color.Black,
                            isSelected = currentColor == Color.Black,
                            onClick = onColorChange
                        )
                    }
                } else {
                    ColorItem(
                        color = it.color,
                        isSelected = currentColor == it.color,
                        onClick = onColorChange
                    )
                }
            }
        }
        IconButton(
            onClick = onPickColor,
            modifier = Modifier
                .size(LocalAppDimens.current.size)
                .padding(0.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.color_wheel),
                contentDescription = null,
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
            if (isDarkTheme) Color.White
            else Color.Black
        )
    }

    Box(
        modifier = modifier
            .background(color = color, shape = CircleShape)
            .clip(CircleShape)
            .size(LocalAppDimens.current.size)
            .clickable {
                onClick(color)
            }
            .border(
                width = if (selected) 2.dp else 0.dp,
                shape = CircleShape,
                color = selectionAccent
            )

    )
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
            onColorChange = {},
            onPickColor = {}
        )
    }
}