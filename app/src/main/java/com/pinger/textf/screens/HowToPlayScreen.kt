package com.pinger.textf.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
import com.pinger.textf.ui.theme.GameFont

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    GameBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                ScreenTitle(text = "How To Play")
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.fillMaxWidth(0.48f))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mini demo board
                DemoBoard(modifier = Modifier.size(180.dp))

                Spacer(modifier = Modifier.height(24.dp))

                val rules = listOf(
                    "Each puzzle has pairs of colored dots on a grid.",
                    "Connect each pair by drawing a path between them.",
                    "Paths can only go horizontally or vertically \u2014 no diagonals.",
                    "Paths cannot cross or overlap each other.",
                    "Fill the grid to complete the level!",
                    "You can redraw a path at any time \u2014 just tap and drag from any dot.",
                    "The faster you solve it, the better your score!"
                )

                rules.forEachIndexed { index, rule ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}.",
                            fontFamily = GameFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFFFDD835),
                            modifier = Modifier.width(32.dp)
                        )
                        Text(
                            text = rule,
                            fontFamily = GameFont,
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun DemoBoard(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.background(Color.Black.copy(0.6f))) {
        val cellSize = size.width / 4f
        val dotRadius = cellSize * 0.2f

        // Grid lines
        for (i in 0..4) {
            val pos = i * cellSize
            drawLine(Color.White.copy(alpha = 0.15f), Offset(pos, 0f), Offset(pos, size.height), 1f)
            drawLine(Color.White.copy(alpha = 0.15f), Offset(0f, pos), Offset(size.width, pos), 1f)
        }

        // Example: Red pair connected
        val red = Color(0xFFE53935)
        val redPath = listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2)
        drawPath(redPath, red, cellSize, dotRadius)

        // Blue pair connected
        val blue = Color(0xFF1E88E5)
        val bluePath = listOf(0 to 3, 0 to 2, 0 to 1)
        drawPath(bluePath, blue, cellSize, dotRadius)

        // Green dots (not connected yet)
        val green = Color(0xFF43A047)
        drawDot(3, 0, green, cellSize, dotRadius)
        drawDot(3, 3, green, cellSize, dotRadius)
    }
}

private fun DrawScope.drawPath(
    path: List<Pair<Int, Int>>,
    color: Color,
    cellSize: Float,
    dotRadius: Float
) {
    for (i in 0 until path.size - 1) {
        val (r1, c1) = path[i]
        val (r2, c2) = path[i + 1]
        drawLine(
            color = color.copy(alpha = 0.8f),
            start = Offset(c1 * cellSize + cellSize / 2, r1 * cellSize + cellSize / 2),
            end = Offset(c2 * cellSize + cellSize / 2, r2 * cellSize + cellSize / 2),
            strokeWidth = dotRadius,
            cap = StrokeCap.Round
        )
    }
    // Draw dots at start and end
    val (sr, sc) = path.first()
    val (er, ec) = path.last()
    drawDot(sr, sc, color, cellSize, dotRadius * 1.2f)
    drawDot(er, ec, color, cellSize, dotRadius * 1.2f)
}

private fun DrawScope.drawDot(
    row: Int, col: Int, color: Color, cellSize: Float, radius: Float
) {
    val center = Offset(col * cellSize + cellSize / 2, row * cellSize + cellSize / 2)
    drawCircle(color.copy(alpha = 0.3f), radius * 1.8f, center)
    drawCircle(color, radius, center)
}