package image.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import image.core.extension.mapSuccess
import image.core.fetcher.rememberTargetFetcher
import image.core.scope.SettingsScope
import image.core.scope.rememberSettings
import image.core.util.Input
import image.core.util.Resource

@Composable
fun asyncPainterResource(
    input: Input,
    settings: SettingsScope.() -> Unit = {}
): Resource<Painter> {

    val settings = rememberSettings(settings)

    val fetcher = rememberTargetFetcher(input::class, settings.fetchers)

    val flow = remember(fetcher, input) { fetcher.fetch(input) }

    val resource by flow.collectAsState(initial = Resource.Loading())

    return resource.mapSuccess { bytes -> resolveAsPainter(bytes, settings.decoders) }
}
