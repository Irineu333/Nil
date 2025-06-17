package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import image.core.compose.asyncPainterResource
import image.core.util.Input
import image.core.util.Resource
import image.decoder.bitmap.extension.bitmap
import image.decoder.gif.extension.gif
import image.decoder.svg.extension.svg
import image.decoder.xml.extension.xml
import image.fetcher.network.extension.network
import image.fetcher.network.extension.request
import image.fetcher.resources.extension.resources

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = asyncPainterResource(
        input = Input.request("https://cataas.com/cat"),
        fetchers = {
            resources()
            network()
        },
        decoders = {
            bitmap()
            gif()
            xml()
            svg()
        }
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
                    CircularProgressIndicator(progress = { progress })
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
