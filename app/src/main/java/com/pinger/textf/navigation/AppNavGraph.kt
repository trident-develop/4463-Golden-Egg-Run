package com.pinger.textf.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pinger.textf.audio.MusicManager
import com.pinger.textf.audio.VibrationHelper
import com.pinger.textf.screens.*
import com.pinger.textf.storage.GamePreferences

@Composable
fun AppNavGraph(
    navController: NavHostController,
    prefs: GamePreferences,
    musicManager: MusicManager,
    vibrationHelper: VibrationHelper
) {
    NavHost(navController = navController, startDestination = NavRoutes.MENU) {
        composable(NavRoutes.MENU) {
            MenuScreen(
                onLeaderboard = { navController.navigate(NavRoutes.LEADERBOARD) },
                onSettings = { navController.navigate(NavRoutes.SETTINGS) },
                onPlay = { navController.navigate(NavRoutes.LEVELS) }
            )
        }
        composable(NavRoutes.LEADERBOARD) {
            LeaderboardScreen(
                prefs = prefs,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                prefs = prefs,
                musicManager = musicManager,
                onHowToPlay = { navController.navigate(NavRoutes.HOW_TO_PLAY) },
                onPrivacyPolicy = { navController.navigate(NavRoutes.PRIVACY_POLICY) },
                onHints = { navController.navigate(NavRoutes.HINTS) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.HOW_TO_PLAY) {
            HowToPlayScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.PRIVACY_POLICY) {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.HINTS) {
            HintsScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.LEVELS) {
            LevelsScreen(
                prefs = prefs,
                onBack = { navController.popBackStack() },
                onLevelSelected = { levelId ->
                    navController.navigate(NavRoutes.game(levelId))
                }
            )
        }
        composable(
            route = NavRoutes.GAME,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            GameScreen(
                levelId = levelId,
                prefs = prefs,
                vibrationHelper = vibrationHelper,
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.popBackStack(NavRoutes.MENU, inclusive = false)
                },
                onNextLevel = { nextId ->
                    navController.popBackStack()
                    navController.navigate(NavRoutes.game(nextId))
                }
            )
        }
    }
}
