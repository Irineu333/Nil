package com.neoutils.nil.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.merge
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.painter.Animatable
import com.neoutils.nil.core.painter.EmptyPainter
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.scope.SettingsScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun asyncPainterResource(
    request: Request,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter,
    settings: @NilDsl SettingsScope.() -> Unit = {}
): PainterResource {

    return rememberMerged(
        resource = painterResource(
            request = request,
            settings = rememberSettings(settings),
        ) {
            collectAsState(PainterResource.Loading()).value
        },
        placeholder = placeholder,
        fallback = fallback
    )
}


@Composable
fun syncPainterResource(
    request: Request,
    fallback: Painter = EmptyPainter,
    settings: @NilDsl SettingsScope.() -> Unit = {}
): PainterResource {

    return rememberMerged(
        resource = painterResource(
            request = request,
            settings = rememberSettings(settings),
        ) {
            runBlocking { first() }
        },
        fallback = fallback
    )
}

@Composable
private fun painterResource(
    request: Request,
    settings: Settings,
    collect: @Composable (Flow<PainterResource>.() -> PainterResource)
): PainterResource {

    val nil = remember(settings) { Nil(settings) }

    val resource = remember(nil, request) { nil.resolve(request) }.collect()

    LaunchedEffect(resource) {
        when (val painter = resource.painter) {
            is Animatable -> {
                withContext(Dispatchers.Default) {
                    painter.animate()
                }
            }
        }
    }

    return resource
}

@Composable
private fun rememberMerged(
    resource: PainterResource,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter
) = remember(resource, placeholder, fallback) {
    resource.merge(
        failure = fallback,
        loading = placeholder
    )
}