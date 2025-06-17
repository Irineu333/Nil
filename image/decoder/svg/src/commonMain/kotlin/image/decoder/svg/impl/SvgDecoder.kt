package image.decoder.svg.impl

import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support

expect class SvgDecoder(density: Density) : Decoder {
    override fun decode(input: ByteArray): PainterProvider
    override fun support(input: ByteArray): Support
}
