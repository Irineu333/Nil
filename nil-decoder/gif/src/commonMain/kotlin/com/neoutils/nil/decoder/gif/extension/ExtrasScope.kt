package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.decoder.gif.model.GifExtra

fun SettingsScope.gif(
    scope: @NilDsl GifExtra.Builder.() -> Unit
) {
    extras.update(GifExtra.ExtrasKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}