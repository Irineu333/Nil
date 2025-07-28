package com.neoutils.nil.interceptor.memoryCache.impl

import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra
import com.neoutils.nil.interceptor.memoryCache.util.LruMemoryCache
import com.neoutils.nil.util.Remember

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    private val caches = Remember<LruMemoryCache>()

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {
        val extra = settings.extras[MemoryCacheExtra.ExtrasKey]

        if (!extra.enabled) return ChainResult.Skip

        val cache = caches(extra) {
            LruMemoryCache(
                maxSize = extra.maxSize
            )
        }

        if (chain.painter == null && cache.has(chain.request)) {

            return ChainResult.Process(
                chain.doCopy(
                    painter = Resource.Result.Success(cache[chain.request])
                )
            )
        }

        val painter = chain.painter ?: return ChainResult.Skip

        cache[chain.request] = painter.getOrElse { return ChainResult.Skip }

        return ChainResult.Skip
    }
}
