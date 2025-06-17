package com.neoutils.nil.decoder.svg.extension

import com.neoutils.nil.decoder.svg.impl.SvgDecoder
import com.neoutils.nil.core.scope.DecodersScope

fun DecodersScope.svg() {
    decoders.add(SvgDecoder())
}