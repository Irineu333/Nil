package com.neoutils.nil.interceptor.memoryCache.impl

import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.composable.CompositeKeyHash
import com.neoutils.nil.core.extension.process
import com.neoutils.nil.core.extension.skip
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra
import com.neoutils.nil.interceptor.memoryCache.util.LruMemoryCache

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    private val caches = mutableMapOf<Int, LruMemoryCache>()

    override suspend fun intercept(
        extras: Extras,
        chain: Chain
    ): ChainResult {
        val config = extras[MemoryCacheExtra.ExtrasKey]

        if (!config.enabled) return chain.skip()

        val key = extras[CompositeKeyHash]

        val cache = caches.getOrPut(key) {
            LruMemoryCache(
                maxSize = config.maxSize
            )
        }

        if (chain.painter == null && cache.has(chain.request)) {

            return chain.process(
                painter = Resource.Result.Success(cache[chain.request])
            )
        }

        val painter = chain.painter ?: return chain.skip()

        cache[chain.request] = painter.getOrElse { return chain.skip() }

        return chain.skip()
    }
}
