package com.florientmanfo.battlesketch.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.florientmanfo.battlesketch.R
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
    onChangeThickness: (Int) -> Unit,
    onChangeDrawingMode: (DrawingMode) -> Unit
) {

    var showThicknessEditor by remember {
        mutableStateOf(false)
    }
    var currentThickness by remember {
        mutableIntStateOf(1)
    }

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
        IconButton(
            modifier = Modifier.size(LocalAppDimens.current.size),
            onClick = onUndo,
            enabled = canUndo
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Undo,
                contentDescription = null
            )
        }
        IconButton(
            modifier = Modifier.size(LocalAppDimens.current.size),
            onClick = onRedo,
            enabled = canRedo
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Redo,
                contentDescription = null
            )
        }
        IconButton(
            modifier = Modifier.size(LocalAppDimens.current.size),
            onClick = onReset,
            enabled = canErase
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        }
        IconButton(
            modifier = Modifier.size(LocalAppDimens.current.size),
            onClick = { onChangeDrawingMode(DrawingMode.Draw) },
        ) {
            Icon(
                painter = painterResource(R.drawable.pencil),
                contentDescription = null,
                tint= Color.Unspecified
            )
        }
        IconButton(
            modifier = Modifier.size(LocalAppDimens.current.size),
            onClick = { onChangeDrawingMode(DrawingMode.Erase) },
            enabled = canErase
        ) {
            Icon(
                painter = painterResource(R.drawable.eraser),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        IconButton(
            modifier = Modifier.size(LocalAppDimens.current.size),
            onClick = {
                showThicknessEditor = !showThicknessEditor
            },
        ) {
            Icon(
                imageVector = Icons.Default.LineWeight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ThicknessEditor(
    modifier: Modifier = Modifier,
    currentThickness: Int = 1,
    onDismissRequest: () -> Unit,
    onThicknessChange: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember {
        MutableInteractionSource()
    }
    var thickness by remember {
        mutableIntStateOf(currentThickness)
    }
    var cursorOffset by remember {
        mutableStateOf(
            Offset(currentThickness.toFloat(), 0f)
        )
    }
    val slideColor = MaterialTheme.colorScheme.primary
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    CustomAlertDialog(
        modifier = modifier,
        title = stringResource(R.string.thickness_dialog_title),
        onConfirmRequest = {
            onThicknessChange(thickness)
            onDismissRequest()
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
                            thickness = position.x.toInt()
                            if (position.x in 0f..boxSize.width.toFloat()) {
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