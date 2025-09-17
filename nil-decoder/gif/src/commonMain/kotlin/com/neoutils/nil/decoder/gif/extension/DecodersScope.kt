package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.DecodersScope
import com.neoutils.nil.decoder.gif.impl.GifDecoder

fun DecodersScope.gif() {
    add(GifDecoder())
}
