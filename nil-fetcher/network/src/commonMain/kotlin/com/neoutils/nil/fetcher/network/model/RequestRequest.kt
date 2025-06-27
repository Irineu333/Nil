package com.neoutils.nil.fetcher.network.model

import com.neoutils.nil.core.util.Request
import io.ktor.http.*

data class RequestRequest(
    val url: String,
    val method: HttpMethod
) : Request