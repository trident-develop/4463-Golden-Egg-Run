package com.pinger.textf.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.pinger.textf.R
import com.pinger.textf.audio.MusicManager
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.MenuButton
import com.pinger.textf.components.ScreenTitle
import com.pinger.textf.components.SquareButton
import com.pinger.textf.storage.GamePreferences
import com.pinger.textf.ui.theme.GameFont

@Composable
fun SettingsScreen(
    prefs: GamePreferences,
    musicManager: MusicManager,
    onHowToPlay: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onHints: () -> Unit,
    onBack: () -> Unit
) {
    var musicEnabled by remember { mutableStateOf(prefs.isMusicEnabled) }
    var vibrationEnabled by remember { mutableStateOf(prefs.isVibrationEnabled) }
    val isInPreview = LocalInspectionMode.current
    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SquareButton(
                        btnRes = R.drawable.back_button,
                        btnClickable = onBack
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ScreenTitle(text = "Settings")
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.fillMaxWidth(0.32f))
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Music toggle
                SettingsToggleRow(
                    label = "Music",
                    isEnabled = musicEnabled,
                    iconOn = R.drawable.music_on,
                    iconOff = R.drawable.music_off,
                    onToggle = {
                        musicEnabled = !musicEnabled
                        musicManager.setEnabled(musicEnabled)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Vibration toggle
                SettingsToggleRow(
                    label = "Vibration",
                    isEnabled = vibrationEnabled,
                    btnMaxWidth = 0.43f,
                    iconOn = R.drawable.vibration_off,
                    iconOff = R.drawable.vibration_on,
                    onToggle = {
                        vibrationEnabled = !vibrationEnabled
                        prefs.isVibrationEnabled = vibrationEnabled
                    }
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Buttons
                MenuButton(text = "Help", onClick = onHints)
                Spacer(modifier = Modifier.height(4.dp))
                MenuButton(text = "How To Play", fontSize = 20.sp, onClick = onHowToPlay)
                Spacer(modifier = Modifier.height(4.dp))
                MenuButton(text = "Privacy Policy", fontSize = 18.sp, onClick = onPrivacyPolicy)
            }
        }

        if (!isInPreview) {
            AndroidView(
                factory = {
                    val adView = AdView(it)
                    adView.setAdSize(AdSize.BANNER)
                    adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"
                    adView.loadAd(AdRequest.Builder().build())
                    adView
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    isEnabled: Boolean,
    iconOn: Int,
    iconOff: Int,
    btnMaxWidth: Float = 0.34f,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = GameFont,
            fontSize = 24.sp,
            color = Color.White
        )
        SquareButton(
            btnRes = if (isEnabled) iconOn else iconOff,
            btnMaxWidth = btnMaxWidth,
            cooldownMillis = 0L,
            btnClickable = onToggle
        )
    }
}