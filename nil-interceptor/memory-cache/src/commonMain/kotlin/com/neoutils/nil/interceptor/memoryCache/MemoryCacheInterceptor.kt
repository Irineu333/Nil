package com.neoutils.nil.interceptor.memoryCache

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

val MemoryCacheExtra = Extras.Key(
    default = Cache.Builder<Request, Painter>()
        .maximumCacheSize(100)
        .build()
)

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val cache = settings.extras[MemoryCacheExtra]

        if (chain.painter.isLoading) {

            val painter = cache.get(chain.request) ?: return flowOf(chain)

            return flowOf(
                chain.copy(
                    painter = PainterResource.Result.Success(painter)
                )
            )
        }

        if (chain.painter.isSuccess) {
            cache.put(chain.request, chain.painter)
        }

        return flowOf(chain)
    }
}
