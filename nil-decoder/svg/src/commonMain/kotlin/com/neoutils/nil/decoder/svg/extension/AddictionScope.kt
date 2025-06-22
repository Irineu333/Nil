package com.neoutils.nil.decoder.svg.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.decoder.svg.impl.SvgDecoder

fun AddictionScope<Decoder<Extra>>.svg() {
    add(SvgDecoder())
}