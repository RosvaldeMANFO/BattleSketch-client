package com.florientmanfo.battlesketch.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens
import com.florientmanfo.battlesketch.ui.theme.smallDimens

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
    modifier: Modifier = Modifier
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
            tint = Color.Unspecified,
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
