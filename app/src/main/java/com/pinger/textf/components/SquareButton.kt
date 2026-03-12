package com.pinger.textf.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun SquareButton(
    modifier: Modifier = Modifier,
    btnRes: Int,
    btnMaxWidth: Float = 0.18f,
    cooldownMillis: Long = 1000L,
    btnEnabled: Boolean = true,
    btnClickable: () -> Unit
) {
    Image(
        painter = painterResource(id = btnRes),
        contentDescription = "Button",
        modifier = modifier
            .fillMaxWidth(btnMaxWidth)
            .aspectRatio(1f)
            .peckPress(
                onPeck = btnClickable,
                cooldownMillis = cooldownMillis,
                isChickenReady = btnEnabled
            )
    )
}
