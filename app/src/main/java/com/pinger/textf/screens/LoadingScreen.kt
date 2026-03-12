package com.pinger.textf.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinger.textf.components.GameBackground
import com.pinger.textf.ui.theme.GameFont
import kotlin.math.cos
import kotlin.math.sin

private data class LoadingDot(
    val color: Color,
    val angle: Float,
    val radius: Float = 80f
)

@Composable
fun LoadingScreen() {
    val dots = remember {
        listOf(
            LoadingDot(Color(0xFFE53935), 0f),
            LoadingDot(Color(0xFF1E88E5), 72f),
            LoadingDot(Color(0xFF43A047), 144f),
            LoadingDot(Color(0xFFFDD835), 216f),
            LoadingDot(Color(0xFFFF8F00), 288f)
        )
    }
    BackHandler(enabled = true) {}
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val lineProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lineProgress"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Dots text animation
    val dotsAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dotsText"
    )
    val loadingDots = ".".repeat(dotsAnim.toInt().coerceAtMost(3))

    GameBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(
                modifier = Modifier.size(320.dp)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val orbitRadius = size.width * 0.3f
                val dotRadius = 26f

                val positions = dots.mapIndexed { index, dot ->
                    val angle = Math.toRadians((dot.angle + rotation).toDouble())
                    val x = centerX + (orbitRadius * cos(angle)).toFloat()
                    val y = centerY + (orbitRadius * sin(angle)).toFloat()
                    Offset(x, y)
                }

                // Draw connecting lines
                val currentLine = lineProgress.toInt() % dots.size
                for (i in dots.indices) {
                    val nextIdx = (i + 1) % dots.size
                    val lineAlpha = if (i == currentLine) glowAlpha else 0.15f
                    val lineWidth = if (i == currentLine) 8f else 6f

                    drawLine(
                        color = dots[i].color.copy(alpha = lineAlpha),
                        start = positions[i],
                        end = positions[nextIdx],
                        strokeWidth = lineWidth,
                        cap = StrokeCap.Round
                    )

                    // Light impulse on active line
                    if (i == currentLine) {
                        val frac = lineProgress - lineProgress.toInt()
                        val impulsePos = Offset(
                            positions[i].x + (positions[nextIdx].x - positions[i].x) * frac,
                            positions[i].y + (positions[nextIdx].y - positions[i].y) * frac
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.7f),
                            radius = 6f,
                            center = impulsePos
                        )
                        drawCircle(
                            color = dots[i].color.copy(alpha = 0.4f),
                            radius = 14f,
                            center = impulsePos
                        )
                    }
                }

                // Draw dots with glow
                for ((idx, pos) in positions.withIndex()) {
                    val p = if (idx % 2 == 0) pulse else 2f - pulse
                    val r = dotRadius * p

                    // Outer glow
                    drawCircle(
                        color = dots[idx].color.copy(alpha = 0.2f),
                        radius = r * 2.5f,
                        center = pos
                    )
                    // Inner glow
                    drawCircle(
                        color = dots[idx].color.copy(alpha = 0.4f),
                        radius = r * 1.5f,
                        center = pos
                    )
                    // Core
                    drawCircle(
                        color = dots[idx].color,
                        radius = r,
                        center = pos
                    )
                    // Highlight
                    drawCircle(
                        color = Color.White.copy(alpha = 0.4f),
                        radius = r * 0.4f,
                        center = Offset(pos.x - r * 0.25f, pos.y - r * 0.25f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Loading$loadingDots",
                fontFamily = GameFont,
                fontSize = 26.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}