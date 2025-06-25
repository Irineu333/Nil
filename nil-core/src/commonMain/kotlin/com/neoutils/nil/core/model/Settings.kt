package com.neoutils.nil.core.model

import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input

class Settings(
    val decoders: List<Decoder<*>>,
    val fetchers: List<Fetcher<Input>>,
    val extras: Extras,
)
