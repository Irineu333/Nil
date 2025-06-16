package image.decoder

import androidx.compose.ui.graphics.painter.Painter
import image.core.decoder.Decoder
import image.core.decoder.Support

class GifDecoder : Decoder {
    override fun decode(input: ByteArray): Painter {
        TODO("Implement")
    }

    override fun support(input: ByteArray): Support {
        TODO("Not yet implemented")
    }
}