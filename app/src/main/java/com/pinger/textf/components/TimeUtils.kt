package com.pinger.textf.components

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val ms = (millis % 1000) / 10
    return if (minutes > 0) {
        "%d:%02d.%02d".format(minutes, seconds, ms)
    } else {
        "%d.%02d s".format(seconds, ms)
    }
}
