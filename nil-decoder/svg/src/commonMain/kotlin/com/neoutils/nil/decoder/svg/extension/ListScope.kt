package com.neoutils.nil.decoder.svg.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.decoder.svg.impl.SvgDecoder

fun ListScope<Decoder>.svg() {
    add(SvgDecoder())
}
