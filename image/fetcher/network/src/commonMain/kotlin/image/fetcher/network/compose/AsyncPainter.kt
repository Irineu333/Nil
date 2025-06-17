package image.fetcher.network.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import image.core.compose.LocalDecoders
import image.core.compose.resolveAsPainter
import image.core.decoder.Decoder
import image.core.extension.mapSuccess
import image.core.extension.onSuccess
import image.core.util.Resource
import image.fetcher.network.impl.NetworkFetcher
import image.fetcher.network.model.MemoryCache
import image.fetcher.network.model.Request

@Composable
fun asyncPainterResource(
    url: String,
    memoryCache: MemoryCache? = remember(url) { MemoryCache(url) },
    networkFetcher: NetworkFetcher = remember { NetworkFetcher() },
    decoders: List<Decoder> = LocalDecoders.current,
    config: Request.Builder.() -> Unit = {}
): Resource<Painter> {

    val cached = remember(memoryCache) { memoryCache?.get() }

    if (cached != null) {
        return Resource.Result.Success(resolveAsPainter(cached, decoders))
    }

    val request = remember(url, config) { Request.Builder().apply(config).build(url) }

    val rawImageFlow = remember(networkFetcher, request) { networkFetcher.fetch(request) }

    val rawImage by rawImageFlow.collectAsState(initial = Resource.Loading())

    return rawImage
        .onSuccess { bytes -> memoryCache?.set(bytes) }
        .mapSuccess { bytes -> resolveAsPainter(bytes, decoders) }
}
