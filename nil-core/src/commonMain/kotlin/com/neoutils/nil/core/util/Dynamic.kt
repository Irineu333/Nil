package com.neoutils.nil.core.util

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor3

object Dynamic {
    val decoders = load<Decoder>()
    val fetchers = load<Fetcher<*>>()
    val interceptors = load<Interceptor3>()
}

internal expect inline  fun <reified T : Any> load(): List<T>
