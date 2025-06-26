package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.decoder.gif.model.GifParams

fun ExtrasScope.gif(
    scope: GifParams.Builder.() -> Unit
) {
    extras.update(GifParams.ExtraKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}