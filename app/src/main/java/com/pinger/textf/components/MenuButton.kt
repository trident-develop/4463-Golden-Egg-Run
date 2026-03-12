package com.pinger.textf.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinger.textf.R
import com.pinger.textf.ui.theme.GameFont

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 26.sp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(height = 80.dp)
            .padding(vertical = 8.dp)
            .pressableWithCooldown(onClick = onClick, enabled = enabled),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.button_blue),
            contentDescription = "button",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = fontSize,
            fontFamily = GameFont,
            modifier = Modifier.padding(bottom = 6.dp)
        )
    }
}
