package com.neoutils.nil.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.neoutils.nil.core.composable.asyncPainterResource
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.decoder.svg.impl.SvgDecoder
import com.neoutils.nil.example.resources.Res
import com.neoutils.nil.example.resources.atom
import com.neoutils.nil.fetcher.resources.extension.resource

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {

    val resource = asyncPainterResource(
        request = Request.resource(Res.drawable.atom),
    ) {
        decoders {
            +SvgDecoder()
        }
    }

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
            Text(resource.throwable.message.orEmpty())
        }
    }
}

