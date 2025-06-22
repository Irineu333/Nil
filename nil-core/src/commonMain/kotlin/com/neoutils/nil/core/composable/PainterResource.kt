@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.composable

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.extension.delegate
import com.neoutils.nil.core.extension.merge
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.painter.Animatable
import com.neoutils.nil.core.scope.SettingsScope
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
        resource = painter,
        placeholder = placeholder,
        fallback = fallback
    )
}

@Composable
fun rememberPainterResource(
    resource: PainterResource,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter
): PainterResource {

    val resource = remember(resource, placeholder, fallback) {
        resource.merge(
            failure = fallback,
            loading = placeholder
        )
    }.delegate()

    LaunchedEffect(resource) {
        when (val painter = resource.painter) {
            is Animatable -> {
                painter.animate()
            }
        }
    }

    return resource
}
