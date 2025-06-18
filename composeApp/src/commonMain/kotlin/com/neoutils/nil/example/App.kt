package com.neoutils.nil.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.compose.asyncPainterResource
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.example.resources.Res
import com.neoutils.nil.example.resources.crazy_cat
import com.neoutils.nil.fetcher.network.extension.request
import com.neoutils.nil.fetcher.resources.extension.resource

@Composable
fun App() = AppTheme {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val resource = asyncPainterResource(
            input = Input.resource(Res.drawable.crazy_cat),
            settings = {
                decoders { gif() }
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

}