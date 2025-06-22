package com.neoutils.nil.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.neoutils.nil.core.composable.asyncPainterResource
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.decoder.lottie.extension.lottie
import com.neoutils.nil.example.resources.Res
import com.neoutils.nil.example.resources.happy_cat
import com.neoutils.nil.fetcher.resources.extension.resource

@Composable
fun App() = AppTheme {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val resource = asyncPainterResource(
            input = Input.resource(Res.drawable.happy_cat),
            settings = {
                decoders {
                    lottie()
                    gif()
                }
            }
        )

        when (resource) {
            is PainterResource.Result.Success -> {
                Image(
                    painter = resource,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is PainterResource.Loading -> {
                if (resource.progress != null) {
                    CircularProgressIndicator(
                        progress = { resource.progress!! }
                    )
                } else {
                    CircularProgressIndicator()
                }
            }

            is PainterResource.Result.Failure -> {
                throw resource.throwable
            }
        }
    }
}