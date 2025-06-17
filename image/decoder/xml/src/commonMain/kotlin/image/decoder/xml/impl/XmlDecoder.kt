package image.decoder.xml.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.decoder.PainterProvider
import image.core.decoder.Support
import org.jetbrains.compose.resources.decodeToImageVector

private val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

class XmlDecoder(
    private val density: Density
) : Decoder {
    override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        return XmlPainterProvider(input.decodeToImageVector(density))
    }

    override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(VECTOR_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}

class XmlPainterProvider(
    private val imageVector: ImageVector
) : PainterProvider {
    @Composable
    override fun provide(): Painter {
        return rememberVectorPainter(imageVector)
    }
}