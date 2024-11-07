import android.graphics.Color.HSVToColor
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ColorWheel(
    modifier: Modifier = Modifier,
    diameter: Dp = LocalAppDimens.provides(largeDimens).value.size,
    currentColor: Color? = null,
    currentOffset: Offset? = null,
    onPickColor: (Color, Offset) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val radius = diameter.value / 2
    val wheelThickness = radius * 0.3f

    var pressOffset by remember {
        mutableStateOf(currentOffset ?: Offset.Zero)
    }
    var selectedColor by remember {
        mutableStateOf(currentColor)
    }


    Canvas(
        modifier = modifier
            .size(diameter)
            .emitDragGesture(interactionSource)
    ) {

        fun pointToColor(pointX: Float, pointY: Float): Color {

            return Color.Black
        }

        scope.collectForPress(interactionSource) { pressPosition ->
            val centerToPress = (pressPosition - center).getDistance()

        }

        // Generate colors for the smooth hue gradient
        val colors = mutableListOf<Color>()
        var hue = 0f
        for (i in 0..720) {
            colors.add(Color(HSVToColor(floatArrayOf(hue, 1f, 1f))))
            hue += 360f / 720
        }

        drawCircle(
            Brush.sweepGradient(colors = colors),
            radius = radius - wheelThickness,
            center = center,
            style = Stroke(width = wheelThickness)
        )

        drawCircle(
            Color.White,
            radius = wheelThickness / 2,
            center = if (currentColor == null) center else pressOffset,
        )
    }
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
        ColorWheel { _, _ -> }
    }
}