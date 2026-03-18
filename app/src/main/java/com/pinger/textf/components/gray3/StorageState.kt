package com.pinger.textf.components.gray3

sealed class StorageState<out T> {
    object Loading : StorageState<Nothing>()
    data class Success<T>(val data: T) : StorageState<T>()
}

data class StorageData(
    val link: String?,
    val stub: Boolean = false,
    val push: Boolean = false,
)