package com.neoutils.nil.core.model

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.util.Input

class Settings(
    val decoders: List<Decoder>,
    val fetchers: List<Fetcher<Input>>
)
