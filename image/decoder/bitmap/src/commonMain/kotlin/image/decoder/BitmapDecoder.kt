package image.decoder

import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import image.core.decoder.Decoder
import image.core.decoder.Support

class BitmapDecoder : Decoder {

    override fun decode(input: ByteArray): Painter {
        return BitmapPainter(input.decodeToImageBitmap())
    }

    override fun support(input: ByteArray): Support {
        return Support.TOTAL
    }
}
