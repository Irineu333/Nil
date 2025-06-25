package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.scope.GifExtraScope

fun ExtrasScope.gif(
    scope: GifExtraScope.() -> Unit
) {
    set(GifParams.ExtraKey, GifExtraScope().apply(scope).build())
}