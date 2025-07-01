package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.impl.GifDecoder

fun ListScope<Decoder>.gif() {
    add(GifDecoder())
}
