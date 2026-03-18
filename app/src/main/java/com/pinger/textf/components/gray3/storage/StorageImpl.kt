package com.pinger.textf.components.gray3.storage

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pinger.textf.components.gray3.FILE_NAME

import io.github.ackeecz.guardian.core.MasterKey
import io.github.ackeecz.guardian.datastore.core.DataStoreCryptoParams
import io.github.ackeecz.guardian.datastore.core.DataStoreEncryptionScheme
import io.github.ackeecz.guardian.datastore.core.DataStoreKeysetConfig
import io.github.ackeecz.guardian.datastore.preferences.createEncrypted

import kotlinx.coroutines.flow.first
import java.io.File

class StorageImpl private constructor(private val context: Context) {

    private val keysetConfig = DataStoreKeysetConfig(
        encryptionScheme = DataStoreEncryptionScheme.AES256_GCM_HKDF_4KB
    )

    private val cryptoParams = DataStoreCryptoParams(
        keysetConfig = keysetConfig,
        getMasterKey = suspend { MasterKey.getOrCreate() }
    )

    private val dataStore = PreferenceDataStoreFactory.createEncrypted(
        context = context,
        cryptoParams = cryptoParams,
        produceFile = { File(context.noBackupFilesDir, "$FILE_NAME.preferences_pb") }
    )

    suspend fun putString(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    suspend fun getString(key: String): String? {
        return dataStore.data.first()[stringPreferencesKey(key)]
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: StorageImpl? = null

        fun getInstance(context: Context): StorageImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StorageImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
