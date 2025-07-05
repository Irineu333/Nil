package com.neoutils.nil.interceptor.memoryCache.model

import com.neoutils.nil.core.contract.Cache
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.interceptor.memoryCache.util.LruMemoryCache

class MemoryCacheExtra(
    val maxSize: Int = 100,
    val enabled: Boolean = true,
) : Cache<Request, PainterResource> by LruMemoryCache(maxSize) {

    fun newBuilder() = Builder(
        maxSize = maxSize,
        enabled = enabled
    )

    class Builder(
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