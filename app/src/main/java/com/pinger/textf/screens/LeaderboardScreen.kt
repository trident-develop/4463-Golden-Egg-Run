package com.pinger.textf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinger.textf.R
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.ScreenTitle
import com.pinger.textf.components.SquareButton
import com.pinger.textf.components.formatTime
import com.pinger.textf.data.LevelProvider
import com.pinger.textf.storage.GamePreferences
import com.pinger.textf.ui.theme.GameFont

@Composable
fun LeaderboardScreen(
    prefs: GamePreferences,
    onBack: () -> Unit
) {
    val levels = LevelProvider.levels

    GameBackground {
        Column(modifier = Modifier.fillMaxSize()) {
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
                ScreenTitle(text = "Leaderboard", fontSize = 32.sp)
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.fillMaxWidth(0.42f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(levels) { _, level ->
                    val bestTime = prefs.getBestTime(level.id)
                    LeaderboardRow(
                        levelId = level.id,
                        bestTimeMillis = bestTime
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    levelId: Int,
    bestTimeMillis: Long
) {
    val isMedal = bestTimeMillis > 0 && levelId <= 3

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.time_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Level $levelId",
                fontFamily = GameFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if (isMedal) Color(0xFFFDD835) else Color.White
            )
            Text(
                text = if (bestTimeMillis > 0) formatTime(bestTimeMillis) else "Not completed",
                fontFamily = GameFont,
                fontSize = 18.sp,
                color = if (bestTimeMillis > 0) Color.White else Color.White.copy(alpha = 0.5f)
            )
        }
    }
}