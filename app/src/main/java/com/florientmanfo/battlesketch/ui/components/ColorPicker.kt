package com.florientmanfo.battlesketch.ui.components

import android.graphics.Color.HSVToColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.florientmanfo.battlesketch.ui.theme.BattleSketchTheme
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.largeDimens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun ColorWheel(
    modifier: Modifier = Modifier,
    wheelRadius: Dp = LocalAppDimens.provides(largeDimens).value.size,
    currentColor: Color? = null,
    currentOffset: Offset? = null,
    onPickColor: (Color, Offset) -> Unit,
) {
    val pointerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val scope = rememberCoroutineScope()
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var pointerOffset by remember {
        mutableStateOf(currentOffset)
    }
    var selectedColor by remember {
        mutableStateOf(currentColor)
    }

    Box(
        modifier = modifier
            .size(wheelRadius)
            .background(Color.Transparent, CircleShape)
            .clip(CircleShape)
            .emitDragGesture(interactionSource)
            .drawWithCache {
                val vertices = 720
                val colors = mutableListOf<Color>()
                var hue = 0f
                for (i in 0..vertices) {
                    colors.add(Color(HSVToColor(floatArrayOf(hue, 1f, 1f))))
                    hue += 360f / 720
                }

                scope.collectForPress(interactionSource) { position ->
                    val center = size.center
                    val motionDistance = sqrt(
                        abs((position.x - center.x).pow(2) + (position.y - center.y).pow(2))
                    )
                    val minSpace = size.width / 2 - size.width / 6 + size.width / 24
                    val maxSpace = size.width / 2 - size.width / 24
                    if (motionDistance in minSpace..maxSpace) {
                        val angle = (atan2(position.y - center.y, position.x - center.x) * (180 / Math.PI)).toFloat()
                        val currentHue = ((angle + 360) % 360)
                        selectedColor = Color(HSVToColor(floatArrayOf(currentHue, 1f, 1f)))
                        pointerOffset = position
                        onPickColor(selectedColor!!, pointerOffset!!)
                    }

                }

                onDrawBehind {
                    drawCircle(
                        brush = Brush.sweepGradient(colors),
                        style = Stroke(size.width / 3)
                    )
                    drawCircle(
                        pointerColor,
                        radius = size.width / 24,
                        style = Fill,
                        center = pointerOffset ?: center
                    )
                }
            }
    )
}

fun CoroutineScope.collectForPress(
    interactionSource: InteractionSource,
    setOffset: (Offset) -> Unit
) {
    launch {
        interactionSource.interactions.collect { interaction ->
            (interaction as? PressInteraction.Press)
                ?.pressPosition
                ?.let(setOffset)
        }
    }
}

private fun Modifier.emitDragGesture(
    interactionSource: MutableInteractionSource
): Modifier = composed {
    val scope = rememberCoroutineScope()
    pointerInput(Unit) {
        detectDragGestures { input, _ ->
            scope.launch {
                interactionSource.emit(PressInteraction.Press(input.position))
            }
        }
    }.clickable(interactionSource, null) {}
}

@Composable
@Preview
fun ColorWheelPreview() {
    BattleSketchTheme {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.margin)
        ) {
            var currentColor by remember {
                mutableStateOf(Color.Black)
            }
            var currentOffset by remember {
                mutableStateOf(Offset.Zero)
            }
            ColorWheel {color, offset ->
                currentColor = color
                currentOffset = offset
            }
            Box(
                modifier = Modifier
                    .size(LocalAppDimens.current.size)
                    .background(currentColor)
            ) {  }
            Text("X: ${currentOffset.x}, Y: ${currentOffset.y}")
        }
    }
}