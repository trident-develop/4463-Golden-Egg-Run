package com.pinger.textf.model

import androidx.compose.ui.graphics.Color

data class ColorPair(
    val color: Color,
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>
)

data class Level(
    val id: Int,
    val gridSize: Int,
    val colorPairs: List<ColorPair>
)
