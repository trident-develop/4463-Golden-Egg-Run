package com.pinger.textf.audio

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.pinger.textf.storage.GamePreferences

class VibrationHelper(private val context: Context) {

    private val prefs = GamePreferences(context)

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun vibrateShort() {
        if (!prefs.isVibrationEnabled) return
        vibrator.vibrate(VibrationEffect.createOneShot(50, 255))
    }

    fun vibrateSuccess() {
        if (!prefs.isVibrationEnabled) return
        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
