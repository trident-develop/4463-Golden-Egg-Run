package com.pinger.textf.model

import androidx.compose.ui.graphics.Color

data class GameState(
    val level: Level,
    val paths: Map<Color, List<Pair<Int, Int>>> = emptyMap(),
    val activeColor: Color? = null,
    val isCompleted: Boolean = false,
    val elapsedMillis: Long = 0L,
    val isPaused: Boolean = false
) {
    fun occupiedCells(): Map<Pair<Int, Int>, Color> {
        val map = mutableMapOf<Pair<Int, Int>, Color>()
        for ((color, path) in paths) {
            for (cell in path) {
                map[cell] = color
            }
        }
        return map
    }

    fun isAllConnected(): Boolean {
        if (paths.size != level.colorPairs.size) return false

        return level.colorPairs.all { pair ->
            val path = paths[pair.color] ?: return@all false
            if (path.size < 2) return@all false

            val first = path.first()
            val last = path.last()

            (first == pair.start && last == pair.end) ||
                    (first == pair.end && last == pair.start)
        }
    }
}
