package com.pinger.textf.components.gray3.push

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import com.pinger.textf.components.gray3.POSTBACK_API_URL
import com.pinger.textf.components.gray3.POSTBACK_FCM_TOKEN_KEY
import com.pinger.textf.components.gray3.POSTBACK_TRACKING_ID_KEY
import com.pinger.textf.components.gray3.PUSH_NOTIFICATION_API_FCM_TOKEN_KEY
import com.pinger.textf.components.gray3.PUSH_NOTIFICATION_API_GADID_KEY
import com.pinger.textf.components.gray3.PUSH_NOTIFICATION_API_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.util.Locale

class PushRegistrationManager(private val context: android.content.Context) {

    private val client = OkHttpClient()

    suspend fun registerDevice() {
        Log.d("TAGG", "Push Register starts...")
        try {
            val gadid = try {FirebaseAnalytics.getInstance(context).appInstanceId.await()} catch (e: Exception) { "error" }
            val rawFcmToken = try {FirebaseMessaging.getInstance().token.await()} catch (e: Exception) { "error" }

            val encodedFcmToken =
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(rawFcmToken, "UTF-8")
                }

            val baseUrl = "https://$PUSH_NOTIFICATION_API_URL"
            val gadidKey = PUSH_NOTIFICATION_API_GADID_KEY
            val fcmTokenKey = PUSH_NOTIFICATION_API_FCM_TOKEN_KEY

            val finalUrl = "$baseUrl?$gadidKey=$gadid&$fcmTokenKey=$encodedFcmToken"

            Log.d("TAGG", "Push Register: $finalUrl")

            val request = Request.Builder()
                .url(finalUrl)
                .addHeader("Accept-Language", Locale.getDefault().toLanguageTag())
                .get()
                .build()


            withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
        } catch (e: Exception) {
            Log.d("TAGG", "Push Error ${e.message}")
        }
    }

    suspend fun sendPostback(trackingId: String) {
        Log.d("TAGG", "Postback starts for ID: $trackingId")
        try {
            val rawFcmToken = try {FirebaseMessaging.getInstance().token.await()} catch (e: Exception) { "error" }

            val encodedFcmToken = withContext(Dispatchers.IO) {
                URLEncoder.encode(rawFcmToken, "UTF-8")
            }

            val baseUrl = "https://$POSTBACK_API_URL"
            val trackingKey = POSTBACK_TRACKING_ID_KEY
            val fcmTokenKey = POSTBACK_FCM_TOKEN_KEY

            val finalUrl = "$baseUrl?$trackingKey=$trackingId&$fcmTokenKey=$encodedFcmToken"

            Log.d("TAGG", "Push Postback: $finalUrl")

            val request = Request.Builder()
                .url(finalUrl)
                .get()
                .build()


            withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
        } catch (e: Exception) {
            Log.d("TAGG", "Postback Error: ${e.message}")
        }
    }
}