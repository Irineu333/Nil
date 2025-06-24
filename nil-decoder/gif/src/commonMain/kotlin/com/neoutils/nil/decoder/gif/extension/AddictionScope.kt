package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Param
import com.neoutils.nil.decoder.gif.impl.GifDecoder
import com.neoutils.nil.decoder.gif.scope.GifExtraScope

fun AddictionScope<Decoder<Param>>.gif() {
    add(GifDecoder())
}

fun AddictionScope<Param>.gif(
    scope: GifExtraScope.() -> Unit
) {
    add(GifExtraScope().apply(scope).build())
}