package image.decoder.bitmap.impl

import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support
import image.decoder.bitmap.format.ImageFormat
import image.decoder.bitmap.provider.BitmapPainterProvider
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder {

    override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        return BitmapPainterProvider(input.decodeToImageBitmap())
    }

    override fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE

        return when (detectFormat(input)) {
            ImageFormat.PNG -> Support.TOTAL
            ImageFormat.JPEG -> Support.TOTAL
            ImageFormat.WEBP -> Support.TOTAL
            ImageFormat.GIF -> Support.PARTIAL
            null -> Support.NONE
        }
    }

    private fun detectFormat(bytes: ByteArray): ImageFormat? {
        return ImageFormat.entries.find { it.matches(bytes) }
    }
}
