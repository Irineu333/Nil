package image.decoder.gif.impl

import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support

expect class GifDecoder() : Decoder {
    override fun decode(input: ByteArray): PainterProvider
    override fun support(input: ByteArray): Support
}
