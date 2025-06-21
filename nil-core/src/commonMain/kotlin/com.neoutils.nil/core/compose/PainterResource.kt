@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.compose

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.extension.delegate
import com.neoutils.nil.core.extension.merge
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.painter.PainterAnimation
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.scope.rememberSettings
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun asyncPainterResource(
    input: Input,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter,
    settings: SettingsScope.() -> Unit = {}
): PainterResource {

    val settings = rememberSettings(settings)

    val flow = remember(settings, input) { Nil(settings).execute(input) }

    val painter by flow.collectAsState(PainterResource.Loading())

    return rememberPainterResource(
        painter = painter,
        placeholder = placeholder,
        fallback = fallback
    )
}

@Composable
fun rememberPainterResource(
    painter: PainterResource,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter
): PainterResource {

    val resolved = remember(painter, placeholder, fallback) {
        painter.merge(
            failure = fallback,
            loading = placeholder
        )
    }.delegate()

    LaunchedEffect(resolved) {
        when (val painter = resolved.painter) {
            is PainterAnimation -> {
                painter.animate()
            }
        }
    }

    return resolved
}
