package com.neoutils.nil.fetcher.network.model

import com.neoutils.nil.core.util.Request
import io.ktor.http.*

data class RequestNetwork(
    val url: String,
    val method: HttpMethod
) : Request