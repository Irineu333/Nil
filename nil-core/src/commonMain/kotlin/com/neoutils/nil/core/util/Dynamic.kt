package com.neoutils.nil.core.util

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor

object Dynamic {
    val decoders = load<Decoder>()
    val fetchers = load<Fetcher<*>>()
    val interceptors = load<Interceptor>()
}

internal expect inline  fun <reified T : Any> load(): List<T>
