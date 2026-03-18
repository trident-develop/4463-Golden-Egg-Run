package com.pinger.textf.components.gray3.urlgenerator

import android.net.Uri
import androidx.core.net.toUri

class UrlGenerator(baseUrl: String) {
    private val uriBuilder = baseUrl.toUri().buildUpon()

    fun addQuery(key: String, value: String?): UrlGenerator {
        if (!value.isNullOrEmpty()) {
            uriBuilder.appendQueryParameter(key, value)
        }
        return this
    }

    fun addQueries(map: Map<String, String>): UrlGenerator {
        map.forEach { (key, value) ->
            addQuery(key, value)
        }
        return this
    }

    fun build(): String = uriBuilder.build().toString()
}