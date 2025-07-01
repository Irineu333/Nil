package com.neoutils.nil.decoder.gif.model

import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.scope.SettingsDsl

data class GifParams(
    val repeatCount: Int = Int.MAX_VALUE
) {

    fun newBuilder() = Builder(
        repeatCount = repeatCount
    )

    class Builder(
        var repeatCount: Int = Int.MAX_VALUE
    ) {
        fun build() = GifParams(
            repeatCount = repeatCount
        )
    }

    companion object {
        val ExtrasKey = Extras.Key(GifParams())
    }
}