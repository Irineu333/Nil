@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.composable

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.extension.merge
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.painter.Animatable
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun asyncPainterResource(
    request: Request,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter,
    settings: SettingsScope.() -> Unit = {}
): PainterResource {

    val settings = rememberSettings(settings)

    val nil = remember(settings) { Nil(settings) }

    val flow = remember(nil, request) { nil.execute(request) }

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
    }

    LaunchedEffect(resource) {
        when (val painter = resource.painter) {
            is Animatable -> {
                painter.animate()
            }
        }
    }

    return resource
}
