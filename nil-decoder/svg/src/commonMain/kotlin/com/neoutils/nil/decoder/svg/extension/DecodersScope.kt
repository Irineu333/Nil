package com.neoutils.nil.decoder.svg.extension

import com.neoutils.nil.core.extension.DecodersScope
import com.neoutils.nil.decoder.svg.impl.SvgDecoder

fun DecodersScope.svg() {
    add(SvgDecoder())
}
