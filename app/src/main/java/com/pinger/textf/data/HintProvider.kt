package com.pinger.textf.data

import androidx.compose.ui.graphics.Color

object HintProvider {

    private val RED = Color(0xFFE53935)
    private val BLUE = Color(0xFF1E88E5)
    private val GREEN = Color(0xFF43A047)
    private val YELLOW = Color(0xFFFDD835)

    data class HintPath(
        val color: Color,
        val path: List<Pair<Int, Int>>
    )

    data class LevelHint(
        val levelId: Int,
        val gridSize: Int,
        val paths: List<HintPath>
    )

    // Verified solutions for levels 1-8
    val hints: List<LevelHint> = listOf(
        // Level 1: RED (0,0)->(1,3), BLUE (3,0)->(2,3)
        LevelHint(1, 4, listOf(
            HintPath(RED, listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2, 1 to 3)),
            HintPath(BLUE, listOf(3 to 0, 3 to 1, 2 to 1, 2 to 2, 2 to 3))
        )),
        // Level 2: RED (0,0)->(1,3), BLUE (2,0)->(3,3)
        LevelHint(2, 4, listOf(
            HintPath(RED, listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 3)),
            HintPath(BLUE, listOf(2 to 0, 3 to 0, 3 to 1, 3 to 2, 3 to 3))
        )),
        // Level 3: RED (0,0)->(2,1), BLUE (0,2)->(1,2), GREEN (3,0)->(3,3)
        LevelHint(3, 4, listOf(
            HintPath(RED, listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1)),
            HintPath(BLUE, listOf(0 to 2, 1 to 2)),
            HintPath(GREEN, listOf(3 to 0, 3 to 1, 3 to 2, 3 to 3))
        )),
        // Level 4: RED (0,0)->(2,1), BLUE (0,2)->(2,2), GREEN (3,0)->(3,3)
        LevelHint(4, 4, listOf(
            HintPath(RED, listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1)),
            HintPath(BLUE, listOf(0 to 2, 0 to 3, 1 to 3, 2 to 3, 2 to 2)),
            HintPath(GREEN, listOf(3 to 0, 3 to 1, 3 to 2, 3 to 3))
        )),
        // Level 5: RED (0,0)->(1,2), BLUE (1,0)->(3,2), GREEN (0,3)->(3,3)
        LevelHint(5, 4, listOf(
            HintPath(RED, listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2)),
            HintPath(BLUE, listOf(1 to 0, 2 to 0, 3 to 0, 3 to 1, 3 to 2)),
            HintPath(GREEN, listOf(0 to 3, 1 to 3, 2 to 3, 3 to 3))
        )),
        // Level 6: RED (0,0)->(3,0), BLUE (0,3)->(3,3), GREEN (1,1)->(2,1)
        LevelHint(6, 4, listOf(
            HintPath(RED, listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)),
            HintPath(BLUE, listOf(0 to 3, 1 to 3, 2 to 3, 3 to 3)),
            HintPath(GREEN, listOf(1 to 1, 1 to 2, 2 to 2, 2 to 1))
        )),
        // Level 7: RED (0,0)->(2,0), BLUE (0,2)->(2,3), GREEN (3,0)->(3,3)
        LevelHint(7, 4, listOf(
            HintPath(RED, listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1, 2 to 0)),
            HintPath(BLUE, listOf(0 to 2, 0 to 3, 1 to 3, 1 to 2, 2 to 2, 2 to 3)),
            HintPath(GREEN, listOf(3 to 0, 3 to 1, 3 to 2, 3 to 3))
        )),
        // Level 8: RED (0,0)->(1,2), BLUE (1,0)->(2,0), GREEN (0,3)->(2,2), YELLOW (3,0)->(3,3)
        LevelHint(8, 4, listOf(
            HintPath(RED, listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2)),
            HintPath(BLUE, listOf(1 to 0, 1 to 1, 2 to 1, 2 to 0)),
            HintPath(GREEN, listOf(0 to 3, 1 to 3, 2 to 3, 2 to 2)),
            HintPath(YELLOW, listOf(3 to 0, 3 to 1, 3 to 2, 3 to 3))
        ))
    )
}
