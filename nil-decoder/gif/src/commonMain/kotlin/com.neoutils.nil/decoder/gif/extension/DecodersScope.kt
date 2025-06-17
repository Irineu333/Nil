package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.decoder.gif.impl.GifDecoder
import com.neoutils.nil.core.scope.DecodersScope

fun DecodersScope.gif() {
    decoders.add(GifDecoder())
}