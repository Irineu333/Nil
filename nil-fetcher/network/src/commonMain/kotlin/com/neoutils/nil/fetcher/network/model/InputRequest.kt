package com.neoutils.nil.fetcher.network.model

import com.neoutils.nil.core.util.Input
import io.ktor.http.*

data class InputRequest(
    val url: String,
    val method: HttpMethod
) : Input