package com.pinger.textf

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.pinger.textf.audio.MusicManager
import com.pinger.textf.audio.VibrationHelper
import com.pinger.textf.navigation.AppNavGraph
import com.pinger.textf.storage.GamePreferences

class MainActivity : ComponentActivity() {

    private lateinit var musicManager: MusicManager
    private lateinit var prefs: GamePreferences
    private lateinit var vibrationHelper: VibrationHelper
    private val windowController by lazy {
        WindowInsetsControllerCompat(window, window.decorView)
    }
    private var multiTouchDetected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prefs = GamePreferences(applicationContext)
        musicManager = MusicManager(applicationContext)
        vibrationHelper = VibrationHelper(applicationContext)

        setContent {
            val navController = rememberNavController()
            AppNavGraph(
                navController = navController,
                prefs = prefs,
                musicManager = musicManager,
                vibrationHelper = vibrationHelper
            )
        }
    }

    override fun onResume() {
        super.onResume()
        windowController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowController.hide(WindowInsetsCompat.Type.systemBars())
        musicManager.resume()
    }

    override fun onPause() {
        super.onPause()
        musicManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicManager.stop()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1) {
            if (!multiTouchDetected) {
                multiTouchDetected = true
                val cancelEvent = MotionEvent.obtain(ev)
                cancelEvent.action = MotionEvent.ACTION_CANCEL
                super.dispatchTouchEvent(cancelEvent)
                cancelEvent.recycle()
            }
            return true
        }
        if (multiTouchDetected) {
            if (ev.actionMasked == MotionEvent.ACTION_UP ||
                ev.actionMasked == MotionEvent.ACTION_CANCEL
            ) {
                multiTouchDetected = false
            }
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
}
