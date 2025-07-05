package com.neoutils.nil.interceptor.memoryCache.model

import com.neoutils.nil.core.util.Extras

data class MemoryCacheExtra(
    val maxSize: Int = 100,
    val enabled: Boolean = true,
) {
    internal fun newBuilder() = Builder(
        maxSize = maxSize,
        enabled = enabled
    )

    class Builder internal constructor(
        var maxSize: Int,
        var enabled: Boolean,
    ) {
        internal fun build() = MemoryCacheExtra(
            maxSize = maxSize,
            enabled = enabled
        )
    }

    companion object {
        val ExtrasKey = Extras.Key(MemoryCacheExtra())
    }
}