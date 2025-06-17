package image.decoder.svg.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import image.core.decoder.Decoder
import image.core.provider.PainterProvider
import image.core.util.Support
import org.jetbrains.compose.resources.decodeToSvgPainter

private val SVG_REGEX = Regex(pattern = "<svg[\\s\\S]+/>[\\s\\S]+</svg>")

actual class SvgDecoder : Decoder {

    private lateinit var density: Density

    @Composable
    override fun Prepare() {
        density = LocalDensity.current
    }

    actual override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        val imageVector = input.decodeToSvgPainter(density)

        return object : PainterProvider {
            @Composable
            override fun provide() = imageVector
        }
    }

    actual override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}