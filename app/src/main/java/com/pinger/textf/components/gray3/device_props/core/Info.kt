package com.pinger.textf.components.gray3.device_props.core

fun interface Info {
    suspend fun collect(vararg args: Any?): String
}