package com.pinger.textf.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class FloatingDot(
    val color: Color,
    val baseX: Float,
    val baseY: Float,
    val radius: Float,
    val speedX: Float,
    val speedY: Float,
    val phaseOffset: Float
)

@Composable
fun ColorFlowBackgroundAnimation(
    modifier: Modifier = Modifier,
    dotCount: Int = 12
) {
    val colors = listOf(
        Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF43A047),
        Color(0xFFFDD835), Color(0xFFFF8F00), Color(0xFF8E24AA),
        Color(0xFF00ACC1), Color(0xFFD81B60)
    )

    val dots = remember {
        List(dotCount) {
            FloatingDot(
                color = colors[it % colors.size],
                baseX = Random.nextFloat(),
                baseY = Random.nextFloat(),
                radius = Random.nextFloat() * 6f + 6f,
                speedX = (Random.nextFloat() - 0.5f) * 0.3f,
                speedY = (Random.nextFloat() - 0.5f) * 0.3f,
                phaseOffset = Random.nextFloat() * 6.28f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bgAnim")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val positions = dots.map { dot ->
            val x = (dot.baseX + sin(time * dot.speedX + dot.phaseOffset) * 0.08f) * w
            val y = (dot.baseY + cos(time * dot.speedY + dot.phaseOffset) * 0.08f) * h
            Offset(x, y)
        }

        // Draw connecting lines between nearby dots
        val connectionDist = w * 0.35f
        for (i in positions.indices) {
            for (j in i + 1 until positions.size) {
                val dist = (positions[i] - positions[j]).getDistance()
                if (dist < connectionDist) {
                    val alpha = ((1f - dist / connectionDist) * 0.3f *
                            (0.5f + 0.5f * sin(time * 2f + dots[i].phaseOffset)))
                        .coerceIn(0f, 0.3f)
                    drawLine(
                        color = dots[i].color.copy(alpha = alpha),
                        start = positions[i],
                        end = positions[j],
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        // Draw dots with pulsing
        for ((idx, pos) in positions.withIndex()) {
            val pulse = 1f + 0.2f * sin(time * 3f + dots[idx].phaseOffset)
            val r = dots[idx].radius * pulse
            drawCircle(
                color = dots[idx].color.copy(alpha = 0.15f),
                radius = r * 2.5f,
                center = pos
            )
            drawCircle(
                color = dots[idx].color.copy(alpha = 0.6f),
                radius = r,
                center = pos
            )
        }
    }
}
