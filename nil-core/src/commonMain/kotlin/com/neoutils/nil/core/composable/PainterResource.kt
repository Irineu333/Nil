@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.composable

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.merge
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.painter.Animatable
import com.neoutils.nil.core.painter.EmptyPainter
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.scope.SettingsScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@Composable
fun painterResource(
    request: Request.Sync,
    fallback: Painter = EmptyPainter,
    settings: @NilDsl SettingsScope.() -> Unit = {}
): PainterResource.Result {

    val settings = rememberSettings(settings)

    val nil = remember(settings) { Nil(settings) }

    var result by remember(nil, request) { mutableStateOf<PainterResource.Result?>(PainterResource.Result.Success(EmptyPainter)) }

    LaunchedEffect(nil, request) {
        result = nil.sync(request)
    }

    LaunchedEffect(result) {
        when (val painter = result?.painter) {
            is Animatable -> {
                withContext(Dispatchers.Default) {
                    painter.animate()
                }
            }
        }
    }

    return result?.let { 
        rememberMerged(
            resource = it,
            fallback = fallback,
        )
    } ?: PainterResource.Result.Failure(
        throwable = RuntimeException("Loading"), 
        painter = fallback
    )
}

@Composable
private fun rememberMerged(
    resource: PainterResource.Result,
    fallback: Painter = EmptyPainter
) = remember(resource, fallback) {
    resource.merge(
        failure = fallback,
    )
}
