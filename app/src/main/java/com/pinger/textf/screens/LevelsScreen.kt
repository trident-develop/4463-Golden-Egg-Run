package com.pinger.textf.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinger.textf.R
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.ScreenTitle
import com.pinger.textf.components.SquareButton
import com.pinger.textf.components.pressableWithCooldown
import com.pinger.textf.data.LevelProvider
import com.pinger.textf.storage.GamePreferences
import com.pinger.textf.ui.theme.GameFont

@Composable
fun LevelsScreen(
    prefs: GamePreferences,
    onBack: () -> Unit,
    onLevelSelected: (Int) -> Unit
) {
    val unlockedLevel = prefs.unlockedLevel
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
                ScreenTitle(text = "Levels")
                Spacer(modifier = Modifier.weight(1f))
                // Invisible spacer for symmetry
                Spacer(modifier = Modifier.fillMaxWidth(0.28f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Level grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(levels) { level ->
                    val isUnlocked = level.id <= unlockedLevel
                    LevelItem(
                        levelId = level.id,
                        isUnlocked = isUnlocked,
                        onClick = { if (isUnlocked) onLevelSelected(level.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelItem(
    levelId: Int,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    var shakeTriggered by remember { mutableStateOf(false) }
    val shakeOffset by animateFloatAsState(
        targetValue = if (shakeTriggered) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        finishedListener = { shakeTriggered = false },
        label = "shake"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                if (!isUnlocked && shakeTriggered) {
                    translationX = shakeOffset * 8f
                }
            }
            .pressableWithCooldown(
                enabled = true,
                onClick = {
                    if (isUnlocked) {
                        onClick()
                    } else {
                        shakeTriggered = true
                    }
                }
            )
    ) {
        Image(
            painter = painterResource(
                if (isUnlocked) R.drawable.level_open_button
                else R.drawable.level_close_button
            ),
            contentDescription = "Level $levelId",
            modifier = Modifier.fillMaxSize(0.85f)
        )

        Text(
            text = "$levelId",
            fontFamily = GameFont,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.White
        )
    }
}