package com.pinger.textf.components.gray3.device_props.core

import android.R
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.google.firebase.analytics.FirebaseAnalytics
import com.pinger.textf.components.gray3.device_props.system.WidevineInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

private const val DELAY_BEFORE_SCORING_MS = 10000L
private const val PAGE_LOADING_TIMEOUT_MS = 30000L

object IpScore {
    private val bgScope = CoroutineScope(Dispatchers.IO)
    private val started = AtomicBoolean(false)
    private val resultSent = AtomicBoolean(false)
    private val startedCleanup = AtomicBoolean(false)
    
    /**
     * !!! Call only on first launch !!!
     */
    operator fun invoke(activity: Activity) = bgScope.launch {
        if (started.compareAndSet(false, true)) {
//          
            delay(DELAY_BEFORE_SCORING_MS)
//          
            withContext(Dispatchers.Main) {
                attachToContentRoot(activity)
            }
        }
    }

    private fun attachToContentRoot(activity: Activity) = runCatching {
        val root = activity.findViewById<ViewGroup>(R.id.content)

        val container = FrameLayout(activity).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val webView = WebView(activity).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                safeBrowsingEnabled = false
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

                // whoer.net
//                
//                webView.evaluateJavascript("document.documentElement.outerHTML") { html ->
//                    
//                    val score = extractScoreFromHtml(html)
//                    
//                    score?.let { sendResult(activity, it) }
//                    cleanup(webView, container, root)
//                }

                // 2ip.io
//                
                runCatching {
                    webView.evaluateJavascript("start()", null)
                }
            }
        }

        container.isVisible = false
        container.addView(webView)
        root.addView(container)

//        
//        webView.loadUrl("https://whoer.net/")

//        
        webView.loadUrl("https://2ip.io/privacy/")

//        webView.handler.postDelayed(PAGE_LOADING_TIMEOUT_MS) {
//            
//            runCatching {
//                if (!startedCleanup.get()) webView.stopLoading()
//            }
//        }

        bgScope.launch(Dispatchers.Main) {
            try {
                var done = false
                while (!done) {
                    val regex = ">\\s*(\\d+)\\s*%".toRegex()
                    webView.evaluateJavascript("document.documentElement.outerHTML") { html ->
                        val score = regex.find(html)?.groupValues?.get(1)?.toInt()
                        if (score != null) {
//                            
                            sendResult(activity, score)
                            done = true
                        }
                    }
                    delay(1000)
                }
            } catch (_: Throwable) {
            } finally {
                cleanup(webView, container, root)
            }
        }
    }

    // whoer.net
//    private fun extractScoreFromHtml(html: String): Int? = runCatching {
//        val match = Regex("""Your disguise:\s*(\d+)%""").find(html)
//        return match?.groupValues?.get(1)?.toInt()
//    }.getOrDefault(null)

    private fun sendResult(context: Context, score: Int) {
        if (resultSent.compareAndSet(false, true)) {
            bgScope.launch {
                runCatching {
                    FirebaseAnalytics.getInstance(context).apply {
                        setUserId(WidevineInfo.getDeviceId())
                        logEvent(
                            "ip_score",
                            Bundle().apply { putInt("disguise_score", score) }
                        )
                    }
                }
            }
        }
    }

    private fun cleanup(webView: WebView, container: FrameLayout, root: ViewGroup) {
        if (startedCleanup.compareAndSet(false, true)) {
            runCatching {
                webView.stopLoading()

                container.removeView(webView)
                root.removeView(container)

                webView.destroy()

//                
            }
        }
    }

}
