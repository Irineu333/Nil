package com.neoutils.nil.core.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Param

fun List<Param>.paramFor(
    decoder: Decoder<Param>
): Param? = find { it::class == decoder.paramType }
