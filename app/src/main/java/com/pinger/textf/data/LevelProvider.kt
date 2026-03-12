package com.pinger.textf.data

import androidx.compose.ui.graphics.Color
import com.pinger.textf.model.ColorPair
import com.pinger.textf.model.Level

object LevelProvider {

    private val RED = Color(0xFFE53935)
    private val BLUE = Color(0xFF1E88E5)
    private val GREEN = Color(0xFF43A047)
    private val YELLOW = Color(0xFFFDD835)
    private val ORANGE = Color(0xFFFF8F00)
    private val PURPLE = Color(0xFF8E24AA)
    private val CYAN = Color(0xFF00ACC1)
    private val PINK = Color(0xFFD81B60)
    private val LIME = Color(0xFFC0CA33)

    val levels: List<Level> = listOf(
        // ========== 4x4 levels (1-10) ==========
        // Level 1: 2 colors, very easy
        Level(1, 4, listOf(
            ColorPair(RED, 0 to 0, 1 to 3),
            ColorPair(BLUE, 3 to 0, 2 to 3)
        )),
        // Level 2: 2 colors
        Level(2, 4, listOf(
            ColorPair(RED, 0 to 0, 1 to 3),
            ColorPair(BLUE, 2 to 0, 3 to 3)
        )),
        // Level 3: 3 colors
        Level(3, 4, listOf(
            ColorPair(RED, 0 to 0, 2 to 1),
            ColorPair(BLUE, 0 to 2, 1 to 2),
            ColorPair(GREEN, 3 to 0, 3 to 3)
        )),
        // Level 4: 3 colors
        Level(4, 4, listOf(
            ColorPair(RED, 0 to 0, 2 to 1),
            ColorPair(BLUE, 0 to 2, 2 to 2),
            ColorPair(GREEN, 3 to 0, 3 to 3)
        )),
        // Level 5: 3 colors
        Level(5, 4, listOf(
            ColorPair(RED, 0 to 0, 1 to 2),
            ColorPair(BLUE, 1 to 0, 3 to 2),
            ColorPair(GREEN, 0 to 3, 3 to 3)
        )),
        // Level 6: 3 colors
        Level(6, 4, listOf(
            ColorPair(RED, 0 to 0, 3 to 0),
            ColorPair(BLUE, 0 to 3, 3 to 3),
            ColorPair(GREEN, 1 to 1, 2 to 1)
        )),
        // Level 7: 3 colors
        Level(7, 4, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 2, 2 to 3),
            ColorPair(GREEN, 3 to 0, 3 to 3)
        )),
        // Level 8: 4 colors
        Level(8, 4, listOf(
            ColorPair(RED, 0 to 0, 1 to 2),
            ColorPair(BLUE, 1 to 0, 2 to 0),
            ColorPair(GREEN, 0 to 3, 2 to 2),
            ColorPair(YELLOW, 3 to 0, 3 to 3)
        )),
        // Level 9: 4 colors
        Level(9, 4, listOf(
            ColorPair(RED, 0 to 0, 0 to 2),
            ColorPair(BLUE, 0 to 3, 2 to 3),
            ColorPair(GREEN, 1 to 0, 3 to 0),
            ColorPair(YELLOW, 3 to 1, 3 to 3)
        )),
        // Level 10: 4 colors
        Level(10, 4, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 2, 1 to 2),
            ColorPair(GREEN, 2 to 1, 3 to 2),
            ColorPair(YELLOW, 3 to 0, 3 to 1)
        )),
        // ========== 5x5 levels (11-20) ==========
        // Level 11: 3 colors
        Level(11, 5, listOf(
            ColorPair(RED, 0 to 0, 1 to 0),
            ColorPair(BLUE, 0 to 3, 2 to 4),
            ColorPair(GREEN, 2 to 0, 3 to 3)
        )),
        // Level 12: 3 colors
        Level(12, 5, listOf(
            ColorPair(RED, 0 to 0, 0 to 4),
            ColorPair(BLUE, 1 to 0, 2 to 0),
            ColorPair(GREEN, 1 to 3, 3 to 2)
        )),
        // Level 13: 4 colors
        Level(13, 5, listOf(
            ColorPair(RED, 0 to 0, 1 to 2),
            ColorPair(BLUE, 0 to 3, 1 to 3),
            ColorPair(GREEN, 2 to 0, 4 to 1),
            ColorPair(YELLOW, 2 to 4, 4 to 3)
        )),
        // Level 14: 4 colors
        Level(14, 5, listOf(
            ColorPair(RED, 0 to 0, 2 to 2),
            ColorPair(BLUE, 0 to 4, 2 to 3),
            ColorPair(GREEN, 0 to 1, 1 to 1),
            ColorPair(YELLOW, 3 to 0, 3 to 1)
        )),
        // Level 15: 4 colors
        Level(15, 5, listOf(
            ColorPair(RED, 0 to 0, 4 to 0),
            ColorPair(BLUE, 0 to 2, 2 to 2),
            ColorPair(GREEN, 0 to 3, 0 to 4),
            ColorPair(YELLOW, 3 to 1, 4 to 1)
        )),
        // Level 16: 4 colors
        Level(16, 5, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 3, 3 to 4),
            ColorPair(GREEN, 2 to 1, 4 to 1),
            ColorPair(YELLOW, 3 to 3, 4 to 2)
        )),
        // Level 17: 5 colors
        Level(17, 5, listOf(
            ColorPair(RED, 0 to 0, 1 to 0),
            ColorPair(BLUE, 0 to 2, 0 to 4),
            ColorPair(GREEN, 1 to 2, 2 to 3),
            ColorPair(YELLOW, 2 to 0, 3 to 0),
            ColorPair(ORANGE, 4 to 0, 3 to 3)
        )),
        // Level 18: 5 colors
        Level(18, 5, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 1, 1 to 1),
            ColorPair(GREEN, 0 to 3, 2 to 4),
            ColorPair(YELLOW, 2 to 1, 3 to 0),
            ColorPair(ORANGE, 3 to 3, 4 to 0)
        )),
        // Level 19: 5 colors
        Level(19, 5, listOf(
            ColorPair(RED, 0 to 0, 1 to 2),
            ColorPair(BLUE, 0 to 3, 1 to 3),
            ColorPair(GREEN, 1 to 0, 4 to 0),
            ColorPair(YELLOW, 2 to 2, 3 to 1),
            ColorPair(ORANGE, 4 to 1, 4 to 4)
        )),
        // Level 20: 5 colors
        Level(20, 5, listOf(
            ColorPair(RED, 0 to 0, 2 to 1),
            ColorPair(BLUE, 0 to 2, 1 to 2),
            ColorPair(GREEN, 2 to 2, 3 to 4),
            ColorPair(YELLOW, 3 to 0, 3 to 3),
            ColorPair(ORANGE, 4 to 0, 4 to 4)
        )),
        // ========== 6x6 levels (21-30) ==========
        // Level 21: 4 colors
        Level(21, 6, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 5, 2 to 3),
            ColorPair(GREEN, 3 to 0, 5 to 0),
            ColorPair(YELLOW, 4 to 5, 5 to 3)
        )),
        // Level 22: 5 colors
        Level(22, 6, listOf(
            ColorPair(RED, 0 to 0, 1 to 0),
            ColorPair(BLUE, 0 to 3, 1 to 3),
            ColorPair(GREEN, 2 to 0, 3 to 0),
            ColorPair(YELLOW, 2 to 2, 3 to 2),
            ColorPair(ORANGE, 4 to 0, 5 to 0)
        )),
        // Level 23: 5 colors
        Level(23, 6, listOf(
            ColorPair(RED, 0 to 0, 3 to 0),
            ColorPair(BLUE, 0 to 2, 2 to 3),
            ColorPair(GREEN, 0 to 4, 2 to 5),
            ColorPair(YELLOW, 3 to 1, 5 to 1),
            ColorPair(ORANGE, 3 to 4, 5 to 2)
        )),
        // Level 24: 6 colors
        Level(24, 6, listOf(
            ColorPair(RED, 0 to 0, 1 to 1),
            ColorPair(BLUE, 0 to 2, 1 to 3),
            ColorPair(GREEN, 0 to 4, 1 to 5),
            ColorPair(YELLOW, 2 to 0, 3 to 1),
            ColorPair(ORANGE, 2 to 3, 3 to 2),
            ColorPair(PURPLE, 4 to 0, 5 to 5)
        )),
        // Level 25: 6 colors
        Level(25, 6, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 2, 1 to 2),
            ColorPair(GREEN, 0 to 4, 2 to 5),
            ColorPair(YELLOW, 3 to 0, 4 to 1),
            ColorPair(ORANGE, 3 to 3, 5 to 5),
            ColorPair(PURPLE, 5 to 0, 5 to 2)
        )),
        // Level 26: 6 colors
        Level(26, 6, listOf(
            ColorPair(RED, 0 to 0, 1 to 2),
            ColorPair(BLUE, 0 to 3, 2 to 5),
            ColorPair(GREEN, 1 to 0, 3 to 0),
            ColorPair(YELLOW, 2 to 2, 4 to 3),
            ColorPair(ORANGE, 3 to 4, 5 to 5),
            ColorPair(PURPLE, 5 to 0, 5 to 3)
        )),
        // Level 27: 6 colors
        Level(27, 6, listOf(
            ColorPair(RED, 0 to 0, 1 to 0),
            ColorPair(BLUE, 0 to 2, 2 to 3),
            ColorPair(GREEN, 0 to 5, 1 to 4),
            ColorPair(YELLOW, 2 to 0, 3 to 1),
            ColorPair(ORANGE, 3 to 3, 5 to 4),
            ColorPair(PURPLE, 4 to 0, 5 to 1)
        )),
        // Level 28: 7 colors
        Level(28, 6, listOf(
            ColorPair(RED, 0 to 0, 0 to 1),
            ColorPair(BLUE, 0 to 3, 1 to 4),
            ColorPair(GREEN, 1 to 0, 2 to 1),
            ColorPair(YELLOW, 1 to 2, 2 to 3),
            ColorPair(ORANGE, 3 to 0, 4 to 0),
            ColorPair(PURPLE, 3 to 2, 4 to 3),
            ColorPair(CYAN, 5 to 0, 5 to 5)
        )),
        // Level 29: 7 colors
        Level(29, 6, listOf(
            ColorPair(RED, 0 to 0, 1 to 1),
            ColorPair(BLUE, 0 to 2, 1 to 2),
            ColorPair(GREEN, 0 to 4, 1 to 5),
            ColorPair(YELLOW, 2 to 0, 3 to 0),
            ColorPair(ORANGE, 2 to 2, 3 to 3),
            ColorPair(PURPLE, 4 to 0, 5 to 1),
            ColorPair(CYAN, 4 to 4, 5 to 5)
        )),
        // Level 30: 7 colors
        Level(30, 6, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 2, 1 to 3),
            ColorPair(GREEN, 0 to 5, 2 to 5),
            ColorPair(YELLOW, 3 to 0, 4 to 1),
            ColorPair(ORANGE, 3 to 3, 4 to 4),
            ColorPair(PURPLE, 3 to 5, 5 to 4),
            ColorPair(CYAN, 5 to 0, 5 to 2)
        )),
        // ========== 7x7 levels (31-40) ==========
        // Level 31: 5 colors
        Level(31, 7, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 3, 2 to 4),
            ColorPair(GREEN, 0 to 6, 2 to 6),
            ColorPair(YELLOW, 3 to 0, 5 to 1),
            ColorPair(ORANGE, 5 to 4, 6 to 6)
        )),
        // Level 32: 6 colors
        Level(32, 7, listOf(
            ColorPair(RED, 0 to 0, 1 to 2),
            ColorPair(BLUE, 0 to 3, 1 to 3),
            ColorPair(GREEN, 0 to 5, 1 to 6),
            ColorPair(YELLOW, 2 to 0, 3 to 1),
            ColorPair(ORANGE, 3 to 3, 4 to 4),
            ColorPair(PURPLE, 5 to 0, 6 to 6)
        )),
        // Level 33: 6 colors
        Level(33, 7, listOf(
            ColorPair(RED, 0 to 0, 2 to 1),
            ColorPair(BLUE, 0 to 2, 1 to 4),
            ColorPair(GREEN, 0 to 6, 2 to 5),
            ColorPair(YELLOW, 3 to 0, 5 to 0),
            ColorPair(ORANGE, 3 to 3, 5 to 4),
            ColorPair(PURPLE, 6 to 0, 6 to 6)
        )),
        // Level 34: 7 colors
        Level(34, 7, listOf(
            ColorPair(RED, 0 to 0, 1 to 1),
            ColorPair(BLUE, 0 to 2, 1 to 3),
            ColorPair(GREEN, 0 to 5, 1 to 6),
            ColorPair(YELLOW, 2 to 0, 3 to 0),
            ColorPair(ORANGE, 2 to 2, 3 to 3),
            ColorPair(PURPLE, 4 to 0, 5 to 1),
            ColorPair(CYAN, 4 to 4, 6 to 6)
        )),
        // Level 35: 7 colors
        Level(35, 7, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 2, 2 to 3),
            ColorPair(GREEN, 0 to 5, 1 to 6),
            ColorPair(YELLOW, 3 to 0, 4 to 1),
            ColorPair(ORANGE, 3 to 3, 4 to 4),
            ColorPair(PURPLE, 5 to 0, 6 to 1),
            ColorPair(CYAN, 5 to 5, 6 to 6)
        )),
        // Level 36: 8 colors
        Level(36, 7, listOf(
            ColorPair(RED, 0 to 0, 0 to 1),
            ColorPair(BLUE, 0 to 3, 1 to 4),
            ColorPair(GREEN, 0 to 6, 1 to 5),
            ColorPair(YELLOW, 1 to 0, 2 to 1),
            ColorPair(ORANGE, 2 to 3, 3 to 2),
            ColorPair(PURPLE, 3 to 4, 4 to 5),
            ColorPair(CYAN, 4 to 0, 5 to 0),
            ColorPair(PINK, 5 to 3, 6 to 6)
        )),
        // Level 37: 8 colors
        Level(37, 7, listOf(
            ColorPair(RED, 0 to 0, 1 to 1),
            ColorPair(BLUE, 0 to 2, 0 to 3),
            ColorPair(GREEN, 0 to 5, 1 to 6),
            ColorPair(YELLOW, 2 to 0, 2 to 1),
            ColorPair(ORANGE, 2 to 3, 3 to 4),
            ColorPair(PURPLE, 3 to 0, 4 to 0),
            ColorPair(CYAN, 4 to 2, 5 to 3),
            ColorPair(PINK, 5 to 5, 6 to 6)
        )),
        // Level 38: 8 colors
        Level(38, 7, listOf(
            ColorPair(RED, 0 to 0, 1 to 0),
            ColorPair(BLUE, 0 to 2, 1 to 3),
            ColorPair(GREEN, 0 to 5, 2 to 6),
            ColorPair(YELLOW, 2 to 0, 3 to 1),
            ColorPair(ORANGE, 2 to 3, 3 to 4),
            ColorPair(PURPLE, 4 to 0, 5 to 0),
            ColorPair(CYAN, 4 to 3, 5 to 4),
            ColorPair(PINK, 6 to 0, 6 to 6)
        )),
        // Level 39: 8 colors
        Level(39, 7, listOf(
            ColorPair(RED, 0 to 0, 2 to 0),
            ColorPair(BLUE, 0 to 2, 1 to 3),
            ColorPair(GREEN, 0 to 5, 1 to 6),
            ColorPair(YELLOW, 3 to 0, 4 to 1),
            ColorPair(ORANGE, 3 to 3, 4 to 4),
            ColorPair(PURPLE, 3 to 6, 5 to 5),
            ColorPair(CYAN, 5 to 0, 6 to 1),
            ColorPair(PINK, 6 to 3, 6 to 6)
        )),
        // Level 40: 9 colors
        Level(40, 7, listOf(
            ColorPair(RED, 0 to 0, 0 to 1),
            ColorPair(BLUE, 0 to 3, 1 to 4),
            ColorPair(GREEN, 0 to 6, 1 to 5),
            ColorPair(YELLOW, 1 to 0, 2 to 1),
            ColorPair(ORANGE, 2 to 3, 3 to 2),
            ColorPair(PURPLE, 3 to 4, 4 to 5),
            ColorPair(CYAN, 4 to 0, 5 to 0),
            ColorPair(PINK, 5 to 3, 6 to 4),
            ColorPair(LIME, 6 to 0, 6 to 2)
        ))
    )

    fun getLevel(id: Int): Level = levels.first { it.id == id }
}
