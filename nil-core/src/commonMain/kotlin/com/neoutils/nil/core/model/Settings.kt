package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.source.Interceptor

class Settings internal constructor(
    val decoders: List<Decoder>,
    val fetchers: List<Fetcher<*>>,
    val interceptors: List<Interceptor>,
    val extras: Extras,
)
