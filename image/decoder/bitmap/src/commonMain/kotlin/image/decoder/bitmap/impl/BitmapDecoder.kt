package image.decoder.bitmap.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import image.core.decoder.Decoder
import image.core.decoder.PainterProvider
import image.core.decoder.Support
import image.decoder.bitmap.format.ImageFormat
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
            ImageFormat.GIF87A, ImageFormat.GIF89A -> Support.PARTIAL
            null -> Support.NONE
        }
    }

    private fun detectFormat(bytes: ByteArray): ImageFormat? {
        return ImageFormat.entries.find { it.matches(bytes) }
    }
}

class BitmapPainterProvider(
    private val imageBitmap: ImageBitmap
): PainterProvider {
    @Composable
    override fun provide(): Painter {
        return remember(imageBitmap) { BitmapPainter(imageBitmap)}
    }
}