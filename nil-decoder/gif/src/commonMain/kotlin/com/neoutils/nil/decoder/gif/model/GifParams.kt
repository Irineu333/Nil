package com.neoutils.nil.decoder.gif.model

import com.neoutils.nil.core.scope.Extras

data class GifParams(
    val repeatCount: Int = Int.MAX_VALUE
) {
    companion object {
        val ExtraKey = Extras.Key(GifParams())
    }
}