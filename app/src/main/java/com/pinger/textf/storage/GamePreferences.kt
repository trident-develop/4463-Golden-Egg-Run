package com.pinger.textf.storage

import android.content.Context
import android.content.SharedPreferences

class GamePreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("color_flow_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MUSIC = "music_enabled"
        private const val KEY_VIBRATION = "vibration_enabled"
        private const val KEY_UNLOCKED_LEVEL = "unlocked_level"
        private const val KEY_BEST_TIME_PREFIX = "best_time_level_"
    }

    var isMusicEnabled: Boolean
        get() = prefs.getBoolean(KEY_MUSIC, true)
        set(value) = prefs.edit().putBoolean(KEY_MUSIC, value).apply()

    var isVibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION, value).apply()

    var unlockedLevel: Int
        get() = prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
        set(value) = prefs.edit().putInt(KEY_UNLOCKED_LEVEL, value).apply()

    fun getBestTime(levelId: Int): Long {
        return prefs.getLong("$KEY_BEST_TIME_PREFIX$levelId", -1L)
    }

    fun saveBestTime(levelId: Int, timeMillis: Long) {
        val current = getBestTime(levelId)
        if (current == -1L || timeMillis < current) {
            prefs.edit().putLong("$KEY_BEST_TIME_PREFIX$levelId", timeMillis).apply()
        }
    }

    fun unlockNextLevel(currentLevel: Int) {
        if (currentLevel >= unlockedLevel) {
            unlockedLevel = currentLevel + 1
        }
    }
}
