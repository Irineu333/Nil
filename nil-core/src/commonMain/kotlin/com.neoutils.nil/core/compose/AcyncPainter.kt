@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.extension.animateAsPainter
import com.neoutils.nil.core.extension.combine
import com.neoutils.nil.core.extension.delegate
import com.neoutils.nil.core.extension.getFrom
import com.neoutils.nil.core.fetcher.rememberTargetFetcher
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.scope.rememberSettings
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

@Composable
fun asyncPainterResource(
    input: Input,
    settings: SettingsScope.() -> Unit = {}
): Resource<NilPainter> {

    val settings = rememberSettings(settings)

    val fetcher = rememberTargetFetcher(input::class, settings.fetchers)

    val flow = remember(fetcher, input) {
        fetcher.fetch(input).mapLatest { data ->
            data.combine { bytes ->
                settings.decoders
                    .getFrom(bytes)
                    .combine { decoder ->
                        decoder.decode(bytes)
                    }
            }
        }
    }

    return flow.collectAsState(
        initial = Resource.Loading()
    ).value.delegate()
}

@Composable
fun asyncAnimatedPainterResource(
    input: Input,
    settings: SettingsScope.() -> Unit = {}
): Resource<Painter> {
    return asyncPainterResource(
        input = input,
        settings = settings
    ).animateAsPainter()
}
