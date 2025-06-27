package com.neoutils.nil.fetcher.network.extension

import androidx.compose.runtime.Stable
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.fetcher.network.model.InputRequest
import io.ktor.http.*

@Stable
fun Input.Companion.network(
    url: String,
    method: HttpMethod = HttpMethod.Get
) = InputRequest(url, method)
