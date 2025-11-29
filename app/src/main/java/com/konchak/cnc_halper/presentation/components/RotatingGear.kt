// app/src/main/java/com/konchak/cnc_halper/presentation/components/RotatingGear.kt
package com.konchak.cnc_halper.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RotatingGear(
    isRotating: Boolean,
    size: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    if (isRotating) {
        Box(modifier = modifier) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Работает",
                modifier = Modifier
                    .size(size)
                    .graphicsLayer {
                        rotationZ = rotation
                        alpha = pulseAlpha
                    }
            )
        }
    }
}
