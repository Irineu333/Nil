package com.neoutils.nil.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.decoder.LocalDecoders
import com.neoutils.nil.core.decoder.rememberTargetDecoder
import com.neoutils.nil.core.extension.mapSuccess
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource

@Composable
fun decode(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Resource<Painter> {

    val decoder = rememberTargetDecoder(input, decoders)

    var painter by remember { mutableStateOf<Resource<PainterProvider>>(Resource.Loading()) }

    LaunchedEffect(input, decoder) {
        painter = decoder.decode(input)
    }

    return painter.mapSuccess {
        it.provide()
    }
}
