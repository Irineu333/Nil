package com.neoutils.nil.interceptor.memoryCache.impl

import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.composable.CompositeKeyHash
import com.neoutils.nil.core.extension.process
import com.neoutils.nil.core.extension.skip
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra
import com.neoutils.nil.interceptor.memoryCache.util.LruMemoryCache

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    private val caches = mutableMapOf<Int, LruMemoryCache>()

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {
        val extra = settings.extras[MemoryCacheExtra.ExtrasKey]

        if (!extra.enabled) return chain.skip()

        val key = settings.extras[CompositeKeyHash]

        val cache = caches.getOrPut(key) {
            LruMemoryCache(
                maxSize = extra.maxSize
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
