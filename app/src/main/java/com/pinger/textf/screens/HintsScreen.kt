package com.pinger.textf.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinger.textf.R
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.ScreenTitle
import com.pinger.textf.components.SquareButton
import com.pinger.textf.data.HintProvider
import com.pinger.textf.ui.theme.GameFont

@Composable
fun HintsScreen(onBack: () -> Unit) {
    val hints = HintProvider.hints

    GameBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SquareButton(
                    btnRes = R.drawable.back_button,
                    btnClickable = onBack
                )
                Spacer(modifier = Modifier.weight(1f))
                ScreenTitle(text = "Hints")
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.fillMaxWidth(0.28f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(hints) { hint ->
                    HintCard(hint = hint)
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun HintCard(hint: HintProvider.LevelHint) {
    val infiniteTransition = rememberInfiniteTransition(label = "hint${hint.levelId}")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotPulse${hint.levelId}"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Level ${hint.levelId}",
            fontFamily = GameFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFFFDD835)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f)
        ) {
            val gs = hint.gridSize
            val cellSize = size.width / gs

            // Grid background cells
            for (row in 0 until gs) {
                for (col in 0 until gs) {
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.1f),
                        topLeft = Offset(col * cellSize + 2f, row * cellSize + 2f),
                        size = Size(cellSize - 4f, cellSize - 4f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }
            }

            // Collect endpoints for each color
            val endpoints = mutableMapOf<Color, MutableSet<Pair<Int, Int>>>()
            for (hintPath in hint.paths) {
                val set = endpoints.getOrPut(hintPath.color) { mutableSetOf() }
                if (hintPath.path.isNotEmpty()) {
                    set.add(hintPath.path.first())
                    set.add(hintPath.path.last())
                }
            }

            // Draw solution paths
            for (hintPath in hint.paths) {
                val path = hintPath.path
                if (path.size < 2) continue

                for (i in 0 until path.size - 1) {
                    val (r1, c1) = path[i]
                    val (r2, c2) = path[i + 1]
                    val start = Offset(c1 * cellSize + cellSize / 2, r1 * cellSize + cellSize / 2)
                    val end = Offset(c2 * cellSize + cellSize / 2, r2 * cellSize + cellSize / 2)

                    // Glow
                    drawLine(
                        color = hintPath.color.copy(alpha = 0.2f),
                        start = start,
                        end = end,
                        strokeWidth = cellSize * 0.5f,
                        cap = StrokeCap.Round
                    )
                    // Main line
                    drawLine(
                        color = hintPath.color.copy(alpha = 0.65f),
                        start = start,
                        end = end,
                        strokeWidth = cellSize * 0.3f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // Draw endpoint dots (pulsing)
            for ((color, cells) in endpoints) {
                for (cell in cells) {
                    drawEndpointDot(cell, color, cellSize, pulse)
                }
            }
        }
    }
}

private fun DrawScope.drawEndpointDot(
    cell: Pair<Int, Int>,
    color: Color,
    cellSize: Float,
    pulse: Float
) {
    val center = Offset(
        cell.second * cellSize + cellSize / 2,
        cell.first * cellSize + cellSize / 2
    )
    val radius = cellSize * 0.26f * pulse

    drawCircle(
        color = color.copy(alpha = 0.2f),
        radius = radius * 2f,
        center = center
    )
    drawCircle(
        color = color.copy(alpha = 0.45f),
        radius = radius * 1.4f,
        center = center
    )
    drawCircle(
        color = color,
        radius = radius,
        center = center
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.35f),
        radius = radius * 0.3f,
        center = Offset(center.x - radius * 0.2f, center.y - radius * 0.2f)
    )
}