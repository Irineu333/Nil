package image.decoder.svg.impl

import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support

expect class SvgDecoder() : Decoder {
    override fun decode(input: ByteArray): PainterProvider
    override fun support(input: ByteArray): Support
}
