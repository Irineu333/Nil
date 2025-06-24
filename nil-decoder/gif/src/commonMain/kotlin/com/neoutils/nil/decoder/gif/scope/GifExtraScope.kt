package com.neoutils.nil.decoder.gif.scope

import com.neoutils.nil.decoder.gif.model.GifParam

class GifExtraScope(
    var repeatCount: Int? = null,
) {
    internal fun build() = GifParam(
        repeatCount = repeatCount,
    )
}