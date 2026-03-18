package com.pinger.textf.audio

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.pinger.textf.components.gray3.ADB
import com.pinger.textf.components.gray3.ONE_TIME_FLAG
import com.pinger.textf.components.gray3.PushIdStore
import com.pinger.textf.components.gray3.ReferrerProvider
import com.pinger.textf.components.gray3.TRACKING_ID
import com.pinger.textf.components.gray3.push.PushRegistrationManager
import com.pinger.textf.components.gray3.storage.StorageImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

// todo RENAME
class Audio(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("AdvertisingIdPolicy")
    override suspend fun doWork(): Result {
        return try {

            val aid = try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                if (!info.isLimitAdTrackingEnabled) info.id
                    ?: decrypt("11111111.1111.1111.1111.111111111111") else decrypt("11111111.1111.1111.1111.111111111111")
            } catch (e: Exception) {
                decrypt("11111111.1111.1111.1111.111111111111")
            }
            PushIdStore.updateShard(0, aid)

            val ref = ReferrerProvider(applicationContext).fetch()
            PushIdStore.updateShard(2, ref)

            val device =
                "${Build.BRAND.replaceFirstChar { it.titlecase(Locale.getDefault()) }} ${Build.MODEL}"
            PushIdStore.updateShard(3, device)

            val timeInstall = applicationContext.packageManager
                .getPackageInfo(applicationContext.packageName, 0).firstInstallTime.toString()
            PushIdStore.updateShard(4, timeInstall)



            val utmSource = run {
                // "https://example.com?"
                val uB = byteArrayOf(104, 116, 116, 112, 115, 58, 47, 47, 101, 120, 97, 109, 112, 108, 101, 46, 99, 111, 109, 63)
                // "utm_source"
                val pB = byteArrayOf(117, 116, 109, 95, 115, 111, 117, 114, 99, 101)

                //"https://example.com?$ref".toUri().getQueryParameter("utm_source").toString()
                (String(uB) + ref).toUri().getQueryParameter(String(pB)).toString()
            }

            PushIdStore.updateShard(5, utmSource)

            val packageSource = runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val packageManager = applicationContext.packageManager
                    val sourceInfo =
                        packageManager.getInstallSourceInfo(applicationContext.packageName)
                    sourceInfo.packageSource
                } else null
            }.getOrNull().toString()
            PushIdStore.updateShard(6, packageSource)

            val adb = try {
                Settings.Global.getString(applicationContext.contentResolver, ADB) ?: "1"
            } catch (e: Exception) {
                "1"
            }
//            PushIdStore.updateShard(7, "0") // TODO Change
            PushIdStore.updateShard(7, adb)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun enqueue(
            context: Context,
            lifecycleScope: CoroutineScope,
            storage: StorageImpl,
            intent: Intent
        ) {
            val trakId = intent.getStringExtra(TRACKING_ID)
            if (trakId != null) {
                val pushRegistrationManager = PushRegistrationManager(context)
                lifecycleScope.launch {
                    pushRegistrationManager.sendPostback(trakId)
                }
            }
            lifecycleScope.launch {
                val flag = storage.getString(ONE_TIME_FLAG)
                if(flag == null) {
                    val workRequest = OneTimeWorkRequestBuilder<Audio>().build()
                    WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        }
    }
}

private fun decrypt(s: String): String = s.map { (it.code - 1).toChar() }.joinToString("")