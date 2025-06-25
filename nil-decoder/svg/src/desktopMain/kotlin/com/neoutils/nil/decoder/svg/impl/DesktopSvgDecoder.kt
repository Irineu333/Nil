package com.neoutils.nil.decoder.svg.impl

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.DesktopSvgPainter
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

class DesktopSvgDecoder(
    private val density: Density
) : Decoder<Any> {

    override val paramsKey = Extras.Key(Any())

    override suspend fun decode(
        input: ByteArray,
        params: Any
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            DesktopSvgPainter(SVGDOM(Data.makeFromBytes(input)), density)
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}
