package com.neoutils.nil.decoder.gif.model

import com.neoutils.nil.core.util.Extras

data class GifExtra(
    val repeatCount: Int = Int.MAX_VALUE
) {
    internal fun newBuilder() = Builder(
        repeatCount = repeatCount
    )

    class Builder(
        var repeatCount: Int
    ) {
        internal fun build() = GifExtra(
            repeatCount = repeatCount
        )
    }

    companion object {
        val ExtrasKey = Extras.Key(GifExtra())
    }
}