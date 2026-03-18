package com.pinger.textf.components.gray3.device_props.sensor

import com.pinger.textf.components.gray3.device_props.core.Info
import com.pinger.textf.components.gray3.device_props.core.DeviceMotionResult

class AccelerometerInfo : Info {
    override suspend fun collect(vararg args: Any?): String {
        return try {
            val deviceMotionResult = args[0] as DeviceMotionResult
            val score = deviceMotionResult.accelScore ?: "undefined"
            "ACC[$score]"
        } catch (e: Throwable) {
            "ACC[undefined]"
        }
    }
}