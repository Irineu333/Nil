package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.decoder.gif.impl.GifDecoder
import com.neoutils.nil.decoder.gif.scope.GifScope

fun AddictionScope<Decoder<Extra>>.gif() {
    add(GifDecoder())
}

fun AddictionScope<Extra>.gif(
    scope: GifScope.() -> Unit
) {
    add(GifScope().apply(scope).build())
}