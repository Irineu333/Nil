package image.decoder

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.decoder.Support

class XmlVectorDecoder(
    private val density: Density
) : Decoder {
    override fun decode(input: ByteArray): Painter {
        TODO("Implement")
    }

    override fun support(input: ByteArray): Support {
        return Support.NONE
    }
}