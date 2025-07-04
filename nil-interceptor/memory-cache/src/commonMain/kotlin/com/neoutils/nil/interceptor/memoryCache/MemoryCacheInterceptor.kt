package com.neoutils.nil.interceptor.memoryCache

import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

val MemoryCacheExtrasKey = Extras.Key(
    default = Cache.Builder<Request, PainterResource>()
        .maximumCacheSize(100)
        .build()
)

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    override fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val cache = settings.extras[MemoryCacheExtrasKey]

        if (chain.painter.isLoading) {

            val painter = cache.get(chain.request) ?: return flowOf(chain)

            return flowOf(
                chain.copy(
                    painter = painter
                )
            )
        }

        if (chain.painter.isSuccess) {
            cache.put(chain.request, chain.painter)
        }

        return flowOf(chain)
    }
}
