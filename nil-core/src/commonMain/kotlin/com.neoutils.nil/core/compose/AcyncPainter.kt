package com.neoutils.nil.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.extension.combine
import com.neoutils.nil.core.fetcher.rememberTargetFetcher
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.scope.rememberSettings
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource

@Composable
fun asyncPainterResource(
    input: Input,
    settings: SettingsScope.() -> Unit = {}
): Resource<Painter> {

    val settings = rememberSettings(settings)

    val fetcher = rememberTargetFetcher(input::class, settings.fetchers)

    val flow = remember(fetcher, input) { fetcher.fetch(input) }

    val resource by flow.collectAsState(initial = Resource.Loading())

    return resource.combine {
        bytes -> decode(bytes, settings.decoders)
    }
}
