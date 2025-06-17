package image.decoder.gif.impl

import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support
import image.decoder.gif.extension.startsWith
import image.decoder.gif.provider.GifPainterProvider
import image.decoder.gif.util.signature
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

// https://en.wikipedia.org/wiki/List_of_file_signatures
private val GIF87A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x37, 0x61)
private val GIF89A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x39, 0x61)

actual class GifDecoder : Decoder {
    actual override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        val data = Data.makeFromBytes(input)

        val codec = Codec.makeFromData(data)

        return GifPainterProvider(codec)
    }

    actual override fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE

        return when {
            input.startsWith(GIF87A_SPEC) -> Support.TOTAL
            input.startsWith(GIF89A_SPEC) -> Support.TOTAL
            else -> Support.NONE
        }
    }
}