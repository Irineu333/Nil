package image.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import image.core.decoder.Decoder
import image.core.decoder.LocalDecoders
import image.core.decoder.rememberTargetDecoder

@Composable
fun resolveAsPainter(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Painter {

    val decoder = rememberTargetDecoder(input, decoders).apply { Prepare() }

    return remember(decoder) { decoder.decode(input) }.provide()
}
