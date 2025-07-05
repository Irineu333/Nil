package com.neoutils.nil.interceptor.memoryCache.impl

import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    override fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val cache = settings.extras[MemoryCacheExtra.Companion.ExtrasKey]

        if (!cache.enabled) return flowOf(chain)

        if (chain.painter.isLoading && cache.has(chain.request)) {

            val painter = cache[chain.request]

            return flowOf(
                chain.copy(
                    painter = painter
                )
            )
        }

        if (chain.painter.isSuccess) {
            cache[chain.request] = chain.painter
        }

        return flowOf(chain)
    }
}