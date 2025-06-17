package image.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import image.core.extension.mapSuccess
import image.core.fetcher.FetchersScope
import image.core.util.Input
import image.core.util.Resource

@Composable
fun asyncPainterResource(
    input: Input,
    decoders: DecodersScope.() -> Unit = {},
    fetchers: FetchersScope.() -> Unit = {}
): Resource<Painter> {

    val decodersScope = DecodersScope.fromLocal()
    val fetchersScope = FetchersScope.fromLocal()

    val decoders = remember(decoders, decodersScope) { decodersScope.apply(decoders).build() }
    val fetchers = remember(fetchers, fetchersScope) { fetchersScope.apply(fetchers).build() }

    val fetcher = remember(fetchers, input) { fetchers.find { it.type == input::class } }

    checkNotNull(fetcher) { "No supported fetcher found" }

    fetcher.Prepare()

    val flow = remember(fetcher, input) { fetcher.fetch(input) }

    val resource by flow.collectAsState(initial = Resource.Loading())

    return resource.mapSuccess { bytes -> resolveAsPainter(bytes, decoders) }
}
