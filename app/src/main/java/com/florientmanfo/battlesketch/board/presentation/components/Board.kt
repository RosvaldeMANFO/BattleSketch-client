package com.florientmanfo.battlesketch.board.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.florientmanfo.battlesketch.board.domain.models.DrawingMode
import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import kotlinx.coroutines.launch

@Composable
fun Board(
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onReset: (PathSettings) -> Unit,
    showChat: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    basePath: PathSettings = PathSettings(
        color = if (isSystemInDarkTheme()) Color.White else Color.Black
    ),
    onDrawPath: (PathSettings) -> Unit,
) {
    val paths = remember { mutableStateListOf<PathSettings>() }
    val redoPaths = remember { mutableStateListOf<PathSettings>() }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var toggleDrawingTools by remember { mutableStateOf(false) }
    var painterState by remember { mutableStateOf(basePath) }
    var toggleChat by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = modifier
    ) {
        val eraser = MaterialTheme.colorScheme.surface
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2
            offset = Offset(
                x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        paths.add(
                            painterState.copy(
                                points = mutableListOf(down.position)
                            )
                        )
                        var pointerId: PointerId = down.id
                        while (true) {
                            val event = awaitTouchSlopOrCancellation(pointerId) { change, _ ->
                                if (change.positionChange() != Offset.Zero) change.consume()
                            }
                            if (event != null) {
                                coroutineScope.launch {
                                    paths[paths.size - 1] = painterState.copy(
                                        points = paths.last().points
                                            .toMutableList()
                                            .apply {
                                                add(event.position)
                                            }
                                    )
                                }
                                pointerId = event.id
                            } else break
                        }
                        onDrawPath(paths.last())
                    }
                }
        ) {
            paths.forEach { pathSettings ->
                val path = Path().apply {
                    if (pathSettings.points.size > 1) {
                        pathSettings.points.forEachIndexed { index, point ->
                            if (index == 0) moveTo(point.x, point.y)
                            else lineTo(point.x, point.y)
                        }
                    }
                }
                val style = Stroke(
                    width = pathSettings.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
                if(pathSettings.drawingMode == DrawingMode.Erase){
                    drawPath(
                        path = path,
                        color = eraser,
                        style = style
                    )
                } else {
                    drawPath(
                        path = path,
                        color = pathSettings.color,
                        style = style
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(LocalAppDimens.current.margin)
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
        ) {
            IconButton(
                onClick = { toggleDrawingTools = !toggleDrawingTools }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }

            IconButton(
                onClick = {
                    toggleChat = !toggleChat
                    showChat(toggleChat)
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null)
            }
        }

        AnimatedVisibility(
            visible = toggleDrawingTools,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            DrawingTools(
                canUndo = paths.isNotEmpty(),
                canRedo = redoPaths.isNotEmpty(),
                canErase = paths.isNotEmpty(),
                onUndo = {
                    if (paths.isNotEmpty()) {
                        redoPaths.add(paths.last())
                        paths.removeAt(paths.size - 1)
                        onUndo()
                    }
                },
                onRedo = {
                    if (redoPaths.isNotEmpty()) {
                        paths.add(redoPaths.last())
                        redoPaths.removeAt(redoPaths.size - 1)
                        onRedo()
                    }
                },
                onReset = {
                    paths.clear()
                    redoPaths.clear()
                    painterState = basePath
                    onReset(painterState)
                },
                onChangeThickness = { thickness ->
                    painterState = painterState.copy(strokeWidth = thickness)
                },
                onColorChange = { color, offset ->
                    painterState = painterState.copy(
                        color = color,
                        colorPickerOffset = offset,
                        drawingMode = DrawingMode.Draw
                    )

                },
                onChangeDrawingMode = { mode ->
                    painterState = painterState.copy(drawingMode = mode)
                }
            )
        }
    }
}
