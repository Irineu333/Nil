package image.decoder.xml.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support
import image.decoder.xml.provider.XmlPainterProvider
import org.jetbrains.compose.resources.decodeToImageVector

private val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

class XmlDecoder() : Decoder {

    private lateinit var density: Density

    @Composable
    override fun Prepare() {
        density = LocalDensity.current
    }

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
