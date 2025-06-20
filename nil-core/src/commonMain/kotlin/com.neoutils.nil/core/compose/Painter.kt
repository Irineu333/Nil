package com.neoutils.nil.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.decoder.LocalDecoders
import com.neoutils.nil.core.decoder.rememberTargetDecoder
import com.neoutils.nil.core.extension.fetch
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.util.Resource

@Composable
fun decode(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Resource<NilPainter> {

    val decoder = rememberTargetDecoder(input, decoders)

    return remember(decoder) {
        decoder.fetch(input)
    }.collectAsState(
        initial = Resource.Loading()
    ).value
}
