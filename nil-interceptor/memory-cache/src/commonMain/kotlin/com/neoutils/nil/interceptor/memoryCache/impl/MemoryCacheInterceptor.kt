package com.neoutils.nil.interceptor.memoryCache.impl

import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    override fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val cache = settings.extras[MemoryCacheExtra.ExtrasKey]

        return flowOf(
            when (chain.painter) {
                is PainterResource.Loading if cache.enabled && cache.has(chain.request) -> {
                    chain.copy(
                        painter = cache[chain.request]
                    )
                }

                is PainterResource.Result.Success if cache.enabled -> {
                    cache[chain.request] = chain.painter
                    chain
                }

                else -> chain
            }
        )
    }
}