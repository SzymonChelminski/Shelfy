package com.example.shelfy.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.shelfy.ui.theme.Primary

@Composable
fun ScannerOverlay(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_animation")
    val linePosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "line_animation"
    )

    Canvas(modifier = modifier) {
        val strokeWidth = 4.dp.toPx()
        val cornerRadius = 12.dp.toPx()
        val cornerLength = 40.dp.toPx()
        val size = this.size

        val path = Path().apply {
            moveTo(0f, cornerLength)
            lineTo(0f, cornerRadius)
            arcTo(Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2), 180f, 90f, false)
            lineTo(cornerLength, 0f)

            moveTo(size.width - cornerLength, 0f)
            lineTo(size.width - cornerRadius, 0f)
            arcTo(Rect(size.width - cornerRadius * 2, 0f, size.width, cornerRadius * 2), 270f, 90f, false)
            lineTo(size.width, cornerLength)

            moveTo(size.width, size.height - cornerLength)
            lineTo(size.width, size.height - cornerRadius)
            arcTo(Rect(size.width - cornerRadius * 2, size.height - cornerRadius * 2, size.width, size.height), 0f, 90f, false)
            lineTo(size.width - cornerLength, size.height)

            moveTo(cornerLength, size.height)
            lineTo(cornerRadius, size.height)
            arcTo(Rect(0f, size.height - cornerRadius * 2, cornerRadius * 2, size.height), 90f, 90f, false)
            lineTo(0f, size.height - cornerLength)
        }

        drawPath(
            path = path,
            color = Primary,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        val yOffset = size.height * linePosition
        drawLine(
            color = Primary,
            start = Offset(strokeWidth, yOffset),
            end = Offset(size.width - strokeWidth, yOffset),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}