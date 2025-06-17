package image.decoder.gif.impl

import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support

actual class GifDecoder : Decoder {
    actual override fun decode(input: ByteArray): PainterProvider {
        TODO("Not yet implemented")
    }

    actual override fun support(input: ByteArray): Support {
        return Support.NONE
    }
}