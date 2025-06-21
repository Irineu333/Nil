package com.neoutils.nil.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.neoutils.nil.core.compose.asyncPainterResource
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.fetcher.network.extension.request

@Composable
fun App() = AppTheme {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val resource = asyncPainterResource(
            input = Input.request("https://cataas.com/cat/gif"),
            settings = {
                decoders {
                    gif()
                }
            }
        )

        when (resource) {
            is PainterResource.Result.Success -> {
                Image(
                    painter = resource,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null
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