package com.neoutils.nil.fetcher.network.extension

import androidx.compose.runtime.Stable
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.fetcher.network.model.RequestNetwork
import io.ktor.http.*

@Stable
fun Request.Companion.network(
    url: String,
    method: HttpMethod = HttpMethod.Get,
    key: String =  url
) = RequestNetwork(url, method, key)
