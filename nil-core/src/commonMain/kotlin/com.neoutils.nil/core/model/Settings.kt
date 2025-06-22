package com.neoutils.nil.core.model

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.core.util.Input

class Settings(
    val decoders: List<Decoder<Extra>>,
    val fetchers: List<Fetcher<Input>>,
    val params: List<Extra>
)
