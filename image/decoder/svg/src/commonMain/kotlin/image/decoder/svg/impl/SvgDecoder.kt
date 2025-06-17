package image.decoder.svg.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.decoder.PainterProvider
import image.core.decoder.Support
import org.jetbrains.compose.resources.decodeToSvgPainter

private val SVG_REGEX = Regex(pattern = "<svg[\\s\\S]+/>[\\s\\S]+</svg>")

class SvgDecoder(
    private val density: Density
) : Decoder {
    override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        return SvgPainterProvider(input.decodeToSvgPainter(density))
    }

    override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}

class SvgPainterProvider(
    private val painter: Painter
) : PainterProvider {
    @Composable
    override fun provide() = painter
}