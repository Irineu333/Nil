package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import image.compose.asyncPainterResource
import image.core.util.Resource
import image.decoder.BitmapDecoder
import image.decoder.SvgDecoder
import image.model.MemoryCache

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = asyncPainterResource(
        url = "https://cataas.com/cat",
        memoryCache = MemoryCache.Disabled,
        decoders = listOf(BitmapDecoder(), SvgDecoder(LocalDensity.current))
    )

    when (resource) {
        is Resource.Result.Success<Painter> -> {
            Image(
                painter = resource.data,
                contentDescription = null
            )
        }

        is Resource.Loading -> {
            when (val progress = resource.progress) {
                is Float -> {
                    CircularProgressIndicator(progress = progress)
                }

                null -> {
                    CircularProgressIndicator()
                }
            }
        }

        is Resource.Result.Failure -> {
            throw resource.throwable
        }
    }
}
