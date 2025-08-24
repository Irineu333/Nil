package com.neoutils.nil.core.util

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor

val decoders = mutableListOf<Decoder>()
val fetchers = mutableListOf<Fetcher<*>>()
val interceptors = mutableListOf<Interceptor>()

@Suppress("UNCHECKED_CAST")
internal actual inline fun <reified T : Any> load(): List<T> {
    return when(T::class) {
        Decoder::class -> decoders
        Fetcher::class -> fetchers
        Interceptor::class -> interceptors
        else -> emptyList()
    }.toList() as List<T>
}