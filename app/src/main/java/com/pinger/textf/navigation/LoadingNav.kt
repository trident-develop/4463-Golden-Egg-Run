package com.pinger.textf.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinger.textf.LoadingActivity
import com.pinger.textf.MainActivity
import com.pinger.textf.components.gray3.Gray3
import com.pinger.textf.components.gray3.LINK_STORAGE_KEY
import com.pinger.textf.components.gray3.PUSH_STORAGE_KEY
import com.pinger.textf.components.gray3.PUSH_STORAGE_VALUE_TRUE
import com.pinger.textf.components.gray3.STUB_STORAGE_KEY
import com.pinger.textf.components.gray3.STUB_STORAGE_VALUE_TRUE
import com.pinger.textf.components.gray3.StorageData
import com.pinger.textf.components.gray3.StorageState
import com.pinger.textf.components.gray3.storage.StorageImpl
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

//            LaunchedEffect(Unit) {
//                delay(2000)
//                context.startActivity(Intent(context, MainActivity::class.java))
//                context.finish()
//            }

            // -------------------------
            val conn = remember { context.isFruitConnected() }

            val storage = remember { StorageImpl.getInstance(context) }
            val storageState by produceState<StorageState<StorageData>>(initialValue = StorageState.Loading) {
                val savedLink = storage.getString(LINK_STORAGE_KEY)
                val savedStub = storage.getString(STUB_STORAGE_KEY) == STUB_STORAGE_VALUE_TRUE
                val savedPush = storage.getString(PUSH_STORAGE_KEY) == PUSH_STORAGE_VALUE_TRUE
                val result = StorageData(savedLink, savedStub, savedPush)
                value = StorageState.Success(result)
            }

            if (conn) {
                val toStub = remember { mutableStateOf(false) }
                val toNoInternet = remember { mutableStateOf(false) }

                when (val state = storageState) {
                    is StorageState.Loading -> {
                        
                    }
                    is StorageState.Success -> {
                        val savedLink = state.data.link
                        val savedStub = state.data.stub
                        val savedPush = state.data.push

                        Gray3(savedLink, savedStub, savedPush, storage, toStub, toNoInternet)
                    }
                }

                LaunchedEffect(toStub.value) {
                    if(!toStub.value) return@LaunchedEffect
                    context.startActivity(Intent(context, MainActivity::class.java))
                context.finish()
                }

                LaunchedEffect(toNoInternet.value) {
                    if(!toNoInternet.value) return@LaunchedEffect
                    navController.navigate(NavRoutes.CONNECT) {
                        popUpTo(NavRoutes.LOADING) { inclusive = true }
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    delay(2133)
                    navController.navigate(NavRoutes.CONNECT) {
                        popUpTo(NavRoutes.LOADING) { inclusive = true }
                    }
                }
            }
            // -------------------------

            LoadingScreen()
        }

        composable(NavRoutes.CONNECT) {
            ConnectScreen(navController)
        }
    }
}