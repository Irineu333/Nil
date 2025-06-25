package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.impl.GifDecoder

fun AddictionScope<Decoder<*>>.gif() {
    add(GifDecoder())
}
