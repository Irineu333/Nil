package image.core.decoder

import image.core.provider.PainterProvider
import image.core.util.Support

interface Decoder {
    fun decode(input: ByteArray): PainterProvider
    fun support(input: ByteArray): Support
}
