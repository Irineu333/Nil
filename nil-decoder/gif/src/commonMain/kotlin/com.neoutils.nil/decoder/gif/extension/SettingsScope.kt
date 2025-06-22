package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.decoder.gif.scope.GifScope

fun SettingsScope.gif(
    scope: GifScope.() -> Unit
) {
    extras += GifScope().apply(scope).build()
}