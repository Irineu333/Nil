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
import com.neoutils.image.resources.Res
import com.neoutils.image.resources.crazy_cat
import image.core.util.Resource
import image.decoder.bitmap.impl.BitmapDecoder
import image.decoder.gif.impl.GifDecoder
import image.decoder.svg.impl.SvgDecoder
import image.decoder.xml.impl.XmlDecoder
import image.fetcher.resources.compose.asyncPainterResource

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val density = LocalDensity.current

    val resource = asyncPainterResource(
        res = Res.drawable.crazy_cat,
        decoders = listOf(
            BitmapDecoder(),
            GifDecoder(),
            XmlDecoder(density),
            SvgDecoder(density)
        )
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
