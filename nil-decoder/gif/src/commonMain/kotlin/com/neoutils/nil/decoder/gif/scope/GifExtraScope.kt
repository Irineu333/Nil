package com.neoutils.nil.decoder.gif.scope

import com.neoutils.nil.decoder.gif.model.GifParams

class GifExtraScope(
    var repeatCount: Int = Int.MAX_VALUE,
) {
    internal fun build() = GifParams(
        repeatCount = repeatCount,
    )
}