// app/src/main/java/com/konchak/cnc_halper/presentation/components/WorkingIndicator.kt
package com.konchak.cnc_halper.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun WorkingIndicator(
    isWorking: Boolean,
    size: Dp = 12.dp,
    pulseColor: Color = Color(0xFFFF6B35),
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    if (isWorking) {
        Box(modifier = modifier.size(size)) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                drawCircle(
                    color = pulseColor.copy(alpha = pulseAlpha),
                    radius = size.toPx() / 2,
                    center = center,
                    style = Fill
                )

                // Внутреннее ядро
                drawCircle(
                    color = pulseColor,
                    radius = size.toPx() / 4,
                    center = center,
                    style = Fill
                )
            }
        }
    }
}