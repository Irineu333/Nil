package com.neoutils.nil.core.model

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Param
import com.neoutils.nil.core.util.Input

class Settings(
    val decoders: List<Decoder<Param>>,
    val fetchers: List<Fetcher<Input>>,
    val params: List<Param>,
)
