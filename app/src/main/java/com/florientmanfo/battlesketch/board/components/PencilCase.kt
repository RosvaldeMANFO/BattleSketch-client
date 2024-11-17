package com.florientmanfo.battlesketch.board.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.florientmanfo.battlesketch.R
import com.florientmanfo.battlesketch.core.presentation.components.CustomAlertDialog
import com.florientmanfo.battlesketch.core.presentation.components.CustomIconButton
import com.florientmanfo.battlesketch.core.presentation.components.collectForPress
import com.florientmanfo.battlesketch.core.presentation.components.emitDragGesture
import com.florientmanfo.battlesketch.board.domain.models.DrawingMode
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.smallDimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PencilCase(
    modifier: Modifier = Modifier,
    canUndo: Boolean = false,
    canRedo: Boolean = false,
    canErase: Boolean = false,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onReset: () -> Unit,
    onChangeThickness: (Float) -> Unit,
    onChangeDrawingMode: (DrawingMode) -> Unit
) {
    var showThicknessEditor by remember { mutableStateOf(false) }
    var currentThickness by remember { mutableFloatStateOf(1f) }

    if (showThicknessEditor) {
        ThicknessEditor(
            currentThickness = currentThickness,
            onDismissRequest = { showThicknessEditor = false }
        ) {
            onChangeThickness(it)
            currentThickness = it
        }
    }

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 3,
        verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin),
        horizontalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
    ) {
        CustomIconButton(
            onClick = onUndo,
            enabled = canUndo,
            icon = Icons.AutoMirrored.Filled.Undo
        )
        CustomIconButton(
            onClick = onRedo,
            enabled = canRedo,
            icon = Icons.AutoMirrored.Filled.Redo
        )
        CustomIconButton(
            onClick = onReset,
            enabled = canErase,
            icon = Icons.Default.Refresh
        )
        CustomIconButton(
            onClick = { onChangeDrawingMode(DrawingMode.Draw) },
            painter = painterResource(R.drawable.pencil)
        )
        CustomIconButton(
            onClick = { onChangeDrawingMode(DrawingMode.Erase) },
            enabled = canErase,
            painter = painterResource(R.drawable.eraser)
        )
        CustomIconButton(
            onClick = { showThicknessEditor = !showThicknessEditor },
            icon = Icons.Default.LineWeight
        )
    }
}


@Composable
fun ThicknessEditor(
    modifier: Modifier = Modifier,
    currentThickness: Float = 1f,
    onDismissRequest: () -> Unit,
    onThicknessChange: (Float) -> Unit
) {

    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val interactionSource = remember { MutableInteractionSource() }
    var thickness by remember { mutableFloatStateOf(currentThickness) }
    var cursorOffset by remember { mutableStateOf(Offset(currentThickness, 0f)) }
    val slideColor = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()

    CustomAlertDialog(
        modifier = modifier,
        title = stringResource(R.string.thickness_dialog_title),
        onConfirmRequest = {
            onThicknessChange(thickness)
        },
        onDismissRequest = { onDismissRequest() }
    ) {
        Box(
            modifier = Modifier
                .height(LocalAppDimens.provides(smallDimens).value.size)
                .fillMaxWidth()
                .padding(horizontal = LocalAppDimens.current.margin)
                .onSizeChanged { newSize -> boxSize = newSize }
                .emitDragGesture(interactionSource)
                .drawWithCache {
                    onDrawBehind {
                        scope.collectForPress(interactionSource) { position ->
                            if (position.x in 0f..boxSize.width.toFloat()) {
                                thickness = position.x
                                cursorOffset = Offset(
                                    x = position.x,
                                    y = 0f
                                )
                            }
                        }

                        drawCircle(
                            color = slideColor,
                            center = cursorOffset
                        )
                        drawRoundRect(
                            color = slideColor,
                            topLeft = Offset(0f, -size.height / 5f),
                            size = Size(size.width, size.height / 2.5f),
                            cornerRadius = CornerRadius(50f, 50f)
                        )
                    }
                }
        )
    }
}

@Composable
@Preview
fun PencilCasePreview() {
    BattleSketchTheme {
        PencilCase(
            onUndo = {},
            onRedo = {},
            onChangeThickness = {},
            onChangeDrawingMode = {},
            onReset = {}
        )
    }
}