package com.neoutils.nil.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.composable.asyncPainterResource
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.decoder.svg.extension.svg
import com.neoutils.nil.decoder.xml.extension.xml
import com.neoutils.nil.fetcher.network.extension.network
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val cache = mutableMapOf<Request, Painter>()

class MemoryCacheInterceptorRestore : Interceptor(Level.REQUEST) {
    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        if (!chain.painter.isLoading) {
            return flowOf(chain)
        }

        val painter = cache[chain.request] ?: return flowOf(chain)

        return flowOf(
            chain.copy(
                painter = PainterResource.Result.Success(painter)
            )
        )
    }
}

class MemoryCacheInterceptorSave : Interceptor(Level.PAINTER) {
    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        if (chain.painter.isSuccess) {
            cache[chain.request] = chain.painter
        }

        return flowOf(chain)
    }
}

@Composable
fun App() = AppTheme {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        val resource = asyncPainterResource(
            request = Request.network("https://cataas.com/cat"),
        ) {
            decoders {
                svg()
                xml()
                gif()
            }

            interceptors += listOf(
                MemoryCacheInterceptorSave(),
                MemoryCacheInterceptorRestore()
            )
        }

        when (resource) {
            is PainterResource.Result.Success -> {
                Image(
                    painter = resource,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is PainterResource.Loading -> {
                if (resource.progress != null) {
                    CircularProgressIndicator(
                        progress = { resource.progress!! }
                    )
                } else {
                    CircularProgressIndicator()
                }
            }

            is PainterResource.Result.Failure -> {
                throw resource.throwable
            }
        }
    }
}