package com.neoutils.nil.fetcher.network.model

import com.neoutils.nil.core.util.Input
import io.ktor.http.HttpMethod

data class InputRequest(
    val url: String,
    val headers:  Map<String, String>,
    val method: HttpMethod
) : Input {
    class Builder(
        var headers: Map<String, String> = mapOf(),
        var method: HttpMethod = HttpMethod.Companion.Get
    ) {
        fun build(url: String) = InputRequest(
            headers = headers,
            method = method,
            url = url
        )
    }
}