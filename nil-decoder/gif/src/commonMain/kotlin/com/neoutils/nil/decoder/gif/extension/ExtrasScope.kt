package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.decoder.gif.model.GifExtra

fun ExtrasScope.gif(
    scope: @NilDsl GifExtra.Builder.() -> Unit
) {
    extras.update(GifExtra.ExtrasKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}