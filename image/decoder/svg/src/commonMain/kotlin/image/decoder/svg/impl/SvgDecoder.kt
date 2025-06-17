package image.decoder.svg.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support
import org.jetbrains.compose.resources.decodeToSvgPainter

private val SVG_REGEX = Regex(pattern = "<svg[\\s\\S]+/>[\\s\\S]+</svg>")

class SvgDecoder(
    private val density: Density
) : Decoder {
    override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        val imageVector = input.decodeToSvgPainter(density)

        return object : PainterProvider {
            @Composable
            override fun provide() = imageVector
        }
    }

    override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}
