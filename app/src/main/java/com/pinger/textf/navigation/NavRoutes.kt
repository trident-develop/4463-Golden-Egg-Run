package com.pinger.textf.navigation

object NavRoutes {
    const val MENU = "menu"
    const val LEADERBOARD = "leaderboard"
    const val SETTINGS = "settings"
    const val HOW_TO_PLAY = "how_to_play"
    const val PRIVACY_POLICY = "privacy_policy"
    const val HINTS = "hints"
    const val LEVELS = "levels"
    const val GAME = "game/{levelId}"
    const val CONNECT = "connect"
    const val LOADING = "loading"

    fun game(levelId: Int) = "game/$levelId"
}
