package image.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import image.core.compose.resolveAsPainter
import image.core.decoder.Decoder
import image.core.decoder.LocalDecoders
import image.core.extension.mapSuccess
import image.core.util.Resource
import image.fetcher.ResourcesFetcher
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun asyncPainterResource(
    res: DrawableResource,
    resourceFetcher: ResourcesFetcher = rememberResourceFetcher(),
    decoders : List<Decoder> = LocalDecoders.current
): Resource<Painter> {

    val flow = remember(resourceFetcher, res) { resourceFetcher.fetch(res) }

    val resource by flow.collectAsState(initial = Resource.Loading())

    return resource.mapSuccess { bytes -> resolveAsPainter(bytes, decoders) }
}
