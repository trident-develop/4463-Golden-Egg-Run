package com.pinger.textf.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinger.textf.components.ColorFlowBackgroundAnimation
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.MenuButton
import com.pinger.textf.ui.theme.GameFont

@Composable
fun MenuScreen(
    onLeaderboard: () -> Unit,
    onSettings: () -> Unit,
    onPlay: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "title")
    val titleScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "titleScale"
    )

    GameBackground {
        // Background animation
        ColorFlowBackgroundAnimation(
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(
                    text = "GAME",
                    fontFamily = GameFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 52.sp,
                    color = Color(0xFFFDD835),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(titleScale)
                )
                Text(
                    text = "MENU",
                    fontFamily = GameFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 52.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(titleScale)
                )
            }

            // Buttons section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                MenuButton(text = "Play", onClick = onPlay)
                Spacer(modifier = Modifier.height(4.dp))
                MenuButton(text = "Leaderboard", fontSize = 22.sp, onClick = onLeaderboard)
                Spacer(modifier = Modifier.height(4.dp))
                MenuButton(text = "Settings", onClick = onSettings)
            }
        }
    }
}