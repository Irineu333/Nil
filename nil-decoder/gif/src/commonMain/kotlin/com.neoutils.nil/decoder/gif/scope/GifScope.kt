package com.neoutils.nil.decoder.gif.scope

import com.neoutils.nil.decoder.gif.model.GifParams

class GifScope(
    var repeatCount: Int? = null,
) {
    internal fun build() = GifParams(
        repeatCount = repeatCount,
    )
}