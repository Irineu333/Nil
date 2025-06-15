package core.codec

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.resources.decodeToSvgPainter

class SvgPainterDecoder(
    private val density: Density
) : Decoder<ByteArray, Painter> {
    override fun decode(input: ByteArray): Painter {
        return input.decodeToSvgPainter(density)
    }
}