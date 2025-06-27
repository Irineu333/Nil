package com.neoutils.nil.core.model

import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher

class Settings internal constructor(
    val decoders: List<Decoder>,
    val fetchers: List<Fetcher<*>>,
    val extras: Extras,
)
