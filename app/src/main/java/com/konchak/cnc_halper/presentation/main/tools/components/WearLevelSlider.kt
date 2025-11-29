package com.konchak.cnc_halper.presentation.main.tools.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WearLevelSlider(
    wearLevel: Int,
    onWearLevelChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val levels = listOf(
        WearLevel("Новый", 1, Icons.Default.NewReleases, Color(0xFF4CAF50)),
        WearLevel("Хороший", 2, Icons.Default.CheckCircle, Color(0xFF8BC34A)),
        WearLevel("Изношен", 3, Icons.Default.Warning, Color(0xFFFFC107)),
        WearLevel("Критический", 4, Icons.Default.Error, Color(0xFFF44336))
    )

    val activeColor by animateColorAsState(
        targetValue = levels.find { it.level == wearLevel }?.color ?: Color.Gray,
        label = "ActiveColorAnimation"
    )

    Column(modifier = modifier) {
        Text(
            "Уровень износа: ${levels.find { it.level == wearLevel }?.label ?: ""}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomSlider(
            value = wearLevel,
            onWearLevelChange = onWearLevelChange,
            valueRange = 1..4,
            steps = levels,
            activeColor = activeColor
        )
    }
}

@Composable
private fun CustomSlider(
    value: Int,
    onWearLevelChange: (Int) -> Unit,
    valueRange: IntRange,
    steps: List<WearLevel>,
    activeColor: Color,
    trackHeight: Dp = 12.dp
) {
    val density = LocalDensity.current
    val trackHeightPx = with(density) { trackHeight.toPx() }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val animatedValue by animateFloatAsState(targetValue = value.toFloat(), label = "SliderValueAnimation")
    
    // ✅ ВЫНОСИМ ЦВЕТА ЗА ПРЕДЕЛЫ CANVAS
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(trackHeight + 24.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (canvasSize.width > 0) {
                        val stepWidth = canvasSize.width / (valueRange.last - valueRange.first)
                        val clickedStep = (offset.x / stepWidth).toInt() + valueRange.first
                        onWearLevelChange(clickedStep.coerceIn(valueRange))
                    }
                }
            }
    ) {
        canvasSize = size
        val stepWidth = size.width / (valueRange.last - valueRange.first)
        val thumbRadius = trackHeightPx / 2 * 1.5f
        val animatedActiveWidth = (animatedValue - valueRange.first) * stepWidth

        // Рисуем трек
        drawRoundRect(
            color = surfaceVariantColor,
            size = Size(size.width, trackHeightPx),
            cornerRadius = CornerRadius(trackHeightPx / 2)
        )

        // Рисуем заполненную часть трека
        drawRoundRect(
            color = activeColor,
            size = Size(animatedActiveWidth, trackHeightPx),
            cornerRadius = CornerRadius(trackHeightPx / 2)
        )

        // Рисуем шаги
        steps.forEach { step ->
            val stepX = (step.level - valueRange.first) * stepWidth
            val stepColor = if (step.level <= value) activeColor else surfaceVariantColor
            val stepRadius = if (step.level == value) thumbRadius / 2 else trackHeightPx / 3

            drawCircle(
                color = stepColor,
                radius = stepRadius,
                center = Offset(stepX, size.height / 4)
            )
        }

        // Рисуем "бегунок"
        drawCircle(
            color = activeColor,
            radius = thumbRadius,
            center = Offset(animatedActiveWidth, size.height / 4),
            style = Stroke(width = 4.dp.toPx())
        )
        drawCircle(
            color = surfaceColor,
            radius = thumbRadius - 2.dp.toPx(),
            center = Offset(animatedActiveWidth, size.height / 4)
        )
    }

    // Иконки и подписи под слайдером
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEach { step ->
            val iconColor by animateColorAsState(
                targetValue = if (step.level == value) step.color else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "IconColorAnimation"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Icon(
                    imageVector = step.icon,
                    contentDescription = step.label,
                    tint = iconColor
                )
                Text(
                    step.label,
                    fontSize = 10.sp,
                    color = iconColor,
                    fontWeight = if (step.level == value) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private data class WearLevel(
    val label: String,
    val level: Int,
    val icon: ImageVector,
    val color: Color
)