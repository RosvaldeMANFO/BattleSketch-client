package com.florientmanfo.battlesketch.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.core.presentation.components.model.PathSettings

@Composable
fun Board(
    modifier: Modifier = Modifier,
    basePath: PathSettings = PathSettings(),
    onDrawPath: (PathSettings) -> Unit
) {
    val paths = remember {
        mutableStateListOf<PathSettings>()
    }
    var scale by remember {
        mutableFloatStateOf(1f)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWith = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            val maxX = extraWith / 2
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
                .pointerInput(true) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        val path = basePath.copy(
                            start = change.position - dragAmount,
                            end = change.position
                        )
                        paths.add(path)
                        onDrawPath(path)
                    }
                }
        ) {
            paths.forEach {
                drawLine(
                    color = it.color,
                    start = it.start,
                    end = it.end,
                    strokeWidth = it.thickness.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}