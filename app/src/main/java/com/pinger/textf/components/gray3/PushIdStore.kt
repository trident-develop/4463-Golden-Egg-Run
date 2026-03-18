package com.pinger.textf.components.gray3

import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PushIdStore {
    private val bucketA = AtomicReference<String?>(null)
    private val bucketC = AtomicReference<String?>(null)
    private val bucketD = AtomicReference<String?>(null)
    private val bucketE = AtomicReference<String?>(null)
    private val bucketF = AtomicReference<String?>(null)
    private val bucketG = AtomicReference<String?>(null)
    private val bucketH = AtomicReference<String?>(null)

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private const val MASK = 0x42

    fun updateShard(key: Int, value: String) {
        val obfuscated = obfuscate(value)
        when (key) {
            0 -> bucketA.set(obfuscated)
            2 -> bucketC.set(obfuscated)
            3 -> bucketD.set(obfuscated)
            4 -> bucketE.set(obfuscated)
            5 -> bucketF.set(obfuscated)
            6 -> bucketG.set(obfuscated)
            7 -> bucketH.set(obfuscated)
        }

        if (bucketA.get() != null
            && bucketC.get() != null
            && bucketD.get() != null
            && bucketE.get() != null
            && bucketF.get() != null
            && bucketG.get() != null
            && bucketH.get() != null) {
            _isReady.value = true
        }
    }

    fun getReconstructedData(): PushData? {
        val a = bucketA.get() ?: return null
        val c = bucketC.get() ?: return null
        val d = bucketD.get() ?: return null
        val e = bucketE.get() ?: return null
        val f = bucketF.get() ?: return null
        val g = bucketG.get() ?: return null
        val h = bucketH.get() ?: return null

        return PushData(
            gadid = deobfuscate(a),
            ref = deobfuscate(c),
            device = deobfuscate(d),
            timeInstall = deobfuscate(e),
            utmSource = deobfuscate(f),
            packageSource = deobfuscate(g),
            adb = deobfuscate(h),
        )
    }

    private fun obfuscate(s: String) = s.map { (it.code xor MASK).toChar() }.joinToString("")
    private fun deobfuscate(s: String) = obfuscate(s)
}

data class PushData(
    val gadid: String,
    val ref: String,
    val device: String,
    val timeInstall: String,
    val utmSource: String,
    val packageSource: String,
    val adb: String,
)