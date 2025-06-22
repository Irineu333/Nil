package com.neoutils.nil.core.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Extra

fun List<Extra>.paramsFor(
    decoder: Decoder<Extra>
): Extra? = find { it::class == decoder.extraType }
