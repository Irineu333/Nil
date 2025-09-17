package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor

class Settings internal constructor(
    val interceptors: List<Interceptor>,
    val extras: Extras,
)
