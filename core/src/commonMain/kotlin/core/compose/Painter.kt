package core.compose

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import core.extension.mapSuccess
import core.fetcher.NetworkFetcher
import core.fetcher.ResourceFetcher
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
    networkFetcher: NetworkFetcher = remember { NetworkFetcher() }
): Resource<Painter> {

    val rawImageFlow = remember(networkFetcher, url) { networkFetcher.fetch(url) }

    val rawImage by rawImageFlow.collectAsState(initial = Resource.Loading())

    return rawImage.mapSuccess { it.resolveAsPainter() }
}
