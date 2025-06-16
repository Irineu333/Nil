package core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import core.cache.MemoryCache
import core.extension.mapSuccess
import core.extension.onSuccess
import core.fetcher.NetworkFetcher
import core.fetcher.ResourceFetcher
import core.model.Request
import core.util.Resource
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun asyncPainterResource(
    res: DrawableResource,
    resourceFetcher: ResourceFetcher = rememberResourceFetcher()
): Resource<Painter> {

    val rawImageFlow = remember(resourceFetcher, res) { resourceFetcher.fetch(res) }

    val rawImage by rawImageFlow.collectAsState(initial = Resource.Loading())

    return rawImage.mapSuccess { it.resolveAsPainter() }
}

@Composable
fun asyncPainterResource(
    url: String,
    memoryCache: MemoryCache = remember(url) { MemoryCache(url) },
    networkFetcher: NetworkFetcher = remember { NetworkFetcher() },
    config: Request.Builder.() -> Unit = {}
): Resource<Painter> {

    val cached = remember(memoryCache) { memoryCache.get() }

    if (cached != null) {
        return Resource.Result.Success(cached.resolveAsPainter())
    }

    val request = remember(url, config) { Request.Builder().apply(config).build(url) }

    val rawImageFlow = remember(networkFetcher, request) { networkFetcher.fetch(request) }

    val rawImage by rawImageFlow.collectAsState(initial = Resource.Loading())

    return rawImage
        .onSuccess { memoryCache.set(it) }
        .mapSuccess { it.resolveAsPainter() }
}


