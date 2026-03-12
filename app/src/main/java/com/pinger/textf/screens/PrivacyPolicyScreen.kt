package com.pinger.textf.screens

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.pinger.textf.R
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.ScreenTitle
import com.pinger.textf.components.SquareButton
import com.pinger.textf.ui.theme.Purple40

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    var loadWeb by remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize()) {
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
                    ScreenTitle(text = "Privacy Policy", fontSize = 28.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.fillMaxWidth(0.18f))
                }

                AndroidView(
                    factory = { context ->
                        FrameLayout(context).apply {
                            val webView = WebView(context).apply {
                            setInitialScale(100)
                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        loadWeb = false
                                    }

                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?,
                                        request: WebResourceRequest?
                                    ): Boolean {
                                        return false
                                    }
                                }
                                loadUrl("https://telegra.ph/Privacy-Policy-for-Golden-Egg-Run-03-12")
                            }
                            addView(
                                webView, FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }

        if (loadWeb) {
            LinearProgressIndicator(
                color = Purple40,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .align(Alignment.Center)
            )
        }
    }
}