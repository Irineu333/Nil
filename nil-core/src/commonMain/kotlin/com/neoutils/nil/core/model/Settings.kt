package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor2

class Settings internal constructor(
    val decoders: List<Decoder>,
    val fetchers: List<Fetcher<*>>,
    val interceptors: List<Interceptor2>,
    val extras: Extras,
)
