package image.decoder

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.decoder.Support
import org.jetbrains.compose.resources.decodeToSvgPainter

class SvgDecoder(
    private val density: Density
) : Decoder {
    override fun decode(input: ByteArray): Painter {
        return input.decodeToSvgPainter(density)
    }

    override fun support(input: ByteArray): Support {
        return Support.TOTAL
    }
}