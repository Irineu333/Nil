package com.neoutils.nil.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.decoder.lottie.extension.lottie
import com.neoutils.nil.example.resources.Res
import com.neoutils.nil.example.resources.time
import com.neoutils.nil.fetcher.resources.extension.resource
import com.neoutils.nil.ui.AsyncImage

@Composable
fun App() = AppTheme {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            input = Input.resource(Res.drawable.time),
            colorFilter = ColorFilter.tint(
                color = Color.Blue,
                blendMode = BlendMode.SrcIn
            ),
            settings = {
                decoders {
                    gif()
                    lottie()
                }
            }
        )
    }
}