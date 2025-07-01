package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.core.scope.SettingsDsl
import com.neoutils.nil.decoder.gif.model.GifParams

fun ExtrasScope.gif(
    scope: @SettingsDsl GifParams.Builder.() -> Unit
) {
    extras.update(GifParams.ExtrasKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}