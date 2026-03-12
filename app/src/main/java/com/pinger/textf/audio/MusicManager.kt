package com.pinger.textf.audio

import android.content.Context
import android.media.MediaPlayer
import com.pinger.textf.R
import com.pinger.textf.storage.GamePreferences

class MusicManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val prefs = GamePreferences(context)

    fun start() {
        if (!prefs.isMusicEnabled) return
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_music).apply {
                isLooping = true
                setVolume(0.5f, 0.5f)
            }
        }
        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resume() {
        if (prefs.isMusicEnabled) {
            if (mediaPlayer == null) {
                start()
            } else {
                mediaPlayer?.start()
            }
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun setEnabled(enabled: Boolean) {
        prefs.isMusicEnabled = enabled
        if (enabled) {
            start()
        } else {
            pause()
        }
    }

    val isEnabled: Boolean get() = prefs.isMusicEnabled
}
