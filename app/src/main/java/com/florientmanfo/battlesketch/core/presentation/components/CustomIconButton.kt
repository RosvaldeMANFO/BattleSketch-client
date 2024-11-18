package com.florientmanfo.battlesketch.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    accent: Color = Color.Black,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    painter: Painter? = null,
    isSelected: Boolean = false,
    contentDescription: String? = null
) {
    IconButton(
        modifier = modifier.size(LocalAppDimens.current.size),
        onClick = onClick,
        enabled = enabled
    ) {
        IconContent(
            tint = if(enabled) Color.Unspecified else LocalContentColor.current,
            icon = icon,
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .drawWithContent {
                    drawContent()
                    if (isSelected) {
                        drawSelectionCircle(accent)
                    }
                }
        )
    }
}

@Composable
private fun IconContent(
    icon: ImageVector?,
    painter: Painter?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    if (icon != null) {
        Icon(
            modifier = modifier,
            imageVector = icon,
            contentDescription = contentDescription,
        )
    } else if (painter != null) {
        Icon(
            modifier = modifier,
            painter = painter,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

private fun DrawScope.drawSelectionCircle(accent: Color) {
    drawCircle(
        color = accent,
        radius = size.width / 2,
        style = Stroke(width = size.width / 5)
    )
}
