package com.florientmanfo.battlesketch.board.presentation.components

import android.graphics.PointF
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.board.domain.models.PathSettings
import com.florientmanfo.battlesketch.board.domain.models.SessionData
import com.florientmanfo.battlesketch.core.domain.models.DrawingMode
import com.florientmanfo.battlesketch.core.presentation.components.CustomTextField
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.isTablet
import kotlinx.coroutines.launch

@Composable
fun Board(
    sessionData: SessionData,
    modifier: Modifier = Modifier,
    showDrawingTools: Boolean,
    onUndo: () -> Unit,
    onRedo: (PathSettings) -> Unit,
    onReset: (PathSettings) -> Unit,
    basePath: PathSettings = PathSettings(
        color = MaterialTheme.colorScheme.onSurface
    ),
    onDrawPath: ((PathSettings) -> Unit)? = null,
    onSendMessage: ((String) -> Unit)? = null,
) {
    val context = LocalContext.current
    val paths = remember { mutableStateListOf<PathSettings>() }
    val redoPaths = remember { mutableStateListOf<PathSettings>() }

    LaunchedEffect(sessionData.pathData) {
        paths.clear()
        paths.addAll(sessionData.pathData)
    }

    var showMessageList by remember { mutableStateOf(false) }
    var showPlayerList by remember { mutableStateOf(false) }
    var painterState by remember { mutableStateOf(basePath) }
    val coroutineScope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }

    val eraser = MaterialTheme.colorScheme.surface


    val board: @Composable (
        Modifier, content: @Composable @UiComposable
        (BoxWithConstraintsScope.() -> Unit)
    ) -> Unit = { innerModifier, content ->
        BoxWithConstraints(modifier = innerModifier) {
            var scale by remember { mutableFloatStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
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
            val onSurface = MaterialTheme.colorScheme.onSurface
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
                        if (showDrawingTools) {
                            awaitEachGesture {
                                val down = awaitFirstDown()
                                paths.add(
                                    painterState.copy(
                                        points = mutableListOf(down.position)
                                    )
                                )
                                var pointerId: PointerId = down.id
                                while (true) {
                                    val event =
                                        awaitTouchSlopOrCancellation(pointerId) { change, _ ->
                                            if (change.positionChange() != Offset.Zero) change.consume()
                                        }
                                    if (event != null) {
                                        coroutineScope.launch {
                                            val transformedPoint = Offset(
                                                x = (event.position.x - offset.x) / scale,
                                                y = (event.position.y - offset.y) / scale
                                            )
                                            if (
                                                transformedPoint.x in 0f..constraints.maxWidth.toFloat()
                                                && transformedPoint.y in 0f..constraints.maxHeight.toFloat()
                                            ) {
                                                paths[paths.size - 1] = painterState.copy(
                                                    points = paths.last().points
                                                        .toMutableList()
                                                        .apply { add(transformedPoint) },
                                                )
                                            }
                                        }
                                        pointerId = event.id
                                    } else break
                                }
                                onDrawPath?.invoke(paths.last())
                            }
                        }
                    }
            ) {
                paths.forEach { pathSettings ->
                    val path = Path().apply {
                        if (pathSettings.points.size > 1) {
                            pathSettings.points.forEachIndexed { index, point ->
                                if (index == 0) {
                                    moveTo(point.x, point.y)
                                } else {
                                    val prevPoint = pathSettings.points[index - 1]
                                    val controlPoint = PointF(
                                        (prevPoint.x + point.x) / 2,
                                        (prevPoint.y + point.y) / 2
                                    )
                                    quadraticTo(
                                        controlPoint.x,
                                        controlPoint.y,
                                        point.x,
                                        point.y
                                    )
                                }
                            }
                        }
                    }

                    val style = Stroke(
                        width = pathSettings.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                    if (pathSettings.drawingMode == DrawingMode.Erase) {
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

            Box(
                modifier = Modifier
                    .padding(LocalAppDimens.current.margin)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                this@BoxWithConstraints.content()
            }
        }
    }

    val buttons: @Composable (Modifier) -> Unit = { innerModifier ->
        Column(
            modifier = innerModifier
                .padding(LocalAppDimens.current.margin)
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
        ) {
            IconButton(
                onClick = { showMessageList = !showMessageList }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null)
            }
            IconButton(
                onClick = { showPlayerList = !showPlayerList }
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            }
        }
    }

    val drawingTools: @Composable (Modifier) -> Unit = { innerModifier ->
        DrawingTools(
            modifier = innerModifier,
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
                    onRedo(redoPaths.last())
                    redoPaths.removeAt(redoPaths.size - 1)
                }
            },
            onReset = {
                paths.clear()
                redoPaths.clear()
                painterState = basePath.copy(
                    color = painterState.color,
                    strokeWidth = painterState.strokeWidth
                )
                onReset(painterState)
            },
            onChangeThickness = { strokeWidth ->
                painterState = painterState.copy(strokeWidth = strokeWidth)
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

    val messageTextField: @Composable (Modifier) -> Unit = { innerModifier ->
        CustomTextField(
            modifier = innerModifier.fillMaxWidth(),
            value = message,
            onValueChange = { message = it },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (message.isNotEmpty()) {
                            onSendMessage?.invoke(message)
                            message = ""
                        }
                    }
                ) { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) }
            }
        )
    }

    val messageList: @Composable (Modifier, (@Composable () -> Unit)?) -> Unit =
        { innerModifier, header ->
            MessageList(
                showMessages = showMessageList,
                modifier = innerModifier,
                messages = sessionData.messages,
                headContent = header
            )
        }

    if (isTablet(context)) {
        Row(modifier.fillMaxSize()) {
            showMessageList = true
            messageList(
                Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
            ) {
                PlayerList(
                    players = sessionData.players,
                    modifier = Modifier.fillMaxHeight(0.5f)
                )
            }

            board(
                Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
            ) {
                if (showDrawingTools) {
                    drawingTools(Modifier.align(Alignment.BottomCenter))
                } else {
                    messageTextField(Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    } else {
        board(modifier) {
            buttons(Modifier.align(Alignment.TopEnd))
            AnimatedVisibility(
                visible = showPlayerList,
                enter = slideInHorizontally() + expandVertically() + expandVertically() + fadeIn(),
                exit = slideOutHorizontally() + shrinkVertically() + shrinkHorizontally() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight()
            ) {
                PlayerList(
                    modifier = Modifier.fillMaxSize(),
                    players = sessionData.players
                )
            }
            messageList(Modifier.heightIn(min = 0.dp, max = maxHeight / 2)) {
                if (showDrawingTools) {
                    drawingTools(Modifier.align(Alignment.BottomCenter))
                } else {
                    messageTextField(Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }
}

fun normalizePoint(point: Offset, constraints: Constraints): Offset {
    return Offset(
        x = point.x / constraints.maxWidth,
        y = point.y / constraints.maxHeight
    )
}
fun denormalizePoint(point: Offset, constraints: Constraints): Offset {
    return Offset(
        x = point.x * constraints.maxWidth,
        y = point.y * constraints.maxHeight
    )
}
