package com.florientmanfo.battlesketch.board.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import com.florientmanfo.battlesketch.board.domain.models.Message
import com.florientmanfo.battlesketch.board.domain.models.MessageType
import com.florientmanfo.battlesketch.core.domain.models.Player
import com.florientmanfo.battlesketch.ui.theme.LocalAppDimens

@Composable
fun MessageCard(
    message: Message,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.tertiaryContainer
    val localDimension = LocalAppDimens.current.margin.value

    ListItem(
        colors = ListItemDefaults.colors(containerColor = color),
        modifier = modifier
            .drawWithContent {
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(localDimension),
                    topLeft = Offset(size.width*0.04f, 0f)
                )
                drawPath(
                    path = Path().apply {
                        lineTo(size.width*0.09f, 0f)
                        lineTo(size.width*0.09f, size.height*0.4f)
                    },
                    color = color
                )
                scale(scale = 0.90f){
                    this@drawWithContent.drawContent()
                }
            },
        headlineContent = {
            Text(
                message.content,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        overlineContent = {
            message.sender?.let {
                Text(
                    it.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        leadingContent = {
            Icon(Icons.Default.Person, null)
        },
        trailingContent = {
            message.sender?.let {
                Text(
                    it.score.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}



@Preview
@Composable
fun MessageCardPreview() {
    MessageCard(
        Message(
            "Maison",
            Player("Test", 0),
            MessageType.Suggestion
        )
    )
}