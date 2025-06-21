package com.neoutils.nil.core.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Params

fun List<Params>.paramsFor(
    decoder: Decoder<Params>
): Params? = find { it::class == decoder.type }
