package com.neoutils.nil.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.decoder.LocalDecoders
import com.neoutils.nil.core.decoder.rememberTargetDecoder

@Composable
fun resolveAsPainter(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Painter {

    val decoder = rememberTargetDecoder(input, decoders).apply { Prepare() }

    return remember(decoder) { decoder.decode(input) }.provide()
}
