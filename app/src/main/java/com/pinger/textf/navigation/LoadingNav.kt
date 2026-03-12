package com.pinger.textf.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinger.textf.LoadingActivity
import com.pinger.textf.MainActivity
import com.pinger.textf.screens.ConnectScreen
import com.pinger.textf.screens.LoadingScreen
import com.pinger.textf.screens.isFruitConnected
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun LoadingGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current as LoadingActivity

    NavHost(
        navController = navController,
        startDestination = if (context.isFruitConnected()) NavRoutes.LOADING else NavRoutes.CONNECT
    ) {
        composable(NavRoutes.LOADING) {

            LaunchedEffect(Unit) {
                delay(2000)
                context.startActivity(Intent(context, MainActivity::class.java))
                context.finish()
            }

            LoadingScreen()
        }

        composable(NavRoutes.CONNECT) {
            ConnectScreen(navController)
        }
    }
}