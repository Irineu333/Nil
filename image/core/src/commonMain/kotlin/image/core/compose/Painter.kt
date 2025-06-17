package image.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import image.core.decoder.Decoder
import image.core.util.Support

@Composable
fun resolveAsPainter(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Painter {

    val decoder = remember(input, decoders) {
        decoders
            .filter { it.support(input)  != Support.NONE }
            .maxByOrNull { it.support(input) }
    }

    checkNotNull(decoder) { "No supported decoders found" }

    return remember(decoder) { decoder.decode(input) }.provide()
}
