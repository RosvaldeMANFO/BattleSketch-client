package com.florientmanfo.battlesketch.board.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.core.domain.models.DrawingMode
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.smallDimens

@Composable
fun DrawingTools(
    modifier: Modifier = Modifier,
    initialStrokeWidth: Float = 30f,
    pickerOffset: Offset = Offset.Zero,
    currentColor: Color = MaterialTheme.colorScheme.surface,
    canUndo: Boolean = false,
    canRedo: Boolean = false,
    canErase: Boolean = false,
    onUndo: () -> Unit = {},
    onRedo: () -> Unit = {},
    onReset: () -> Unit = {},
    onChangeStrokeWidth: (Float) -> Unit = { _ -> },
    onColorChange: (Color, Offset?) -> Unit = { _, _ -> },
    onChangeDrawingMode: (DrawingMode) -> Unit = { _ -> }
) {
    Card(
        modifier = modifier.wrapContentSize(),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(LocalAppDimens.current.margin),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PencilCase(
                initialStrokeWidth = initialStrokeWidth,
                canUndo = canUndo,
                canRedo = canRedo,
                canErase = canErase,
                onUndo = onUndo,
                onRedo = onRedo,
                onReset = onReset,
                onChangeStrokeWidth = onChangeStrokeWidth,
                onChangeDrawingMode = onChangeDrawingMode
            )
            Spacer(modifier = Modifier.width(LocalAppDimens.provides(smallDimens).value.margin))
            ColorPalette(
                initColor = currentColor,
                initPickerOffset = pickerOffset,
                onColorChange = onColorChange
            )
        }
    }
}