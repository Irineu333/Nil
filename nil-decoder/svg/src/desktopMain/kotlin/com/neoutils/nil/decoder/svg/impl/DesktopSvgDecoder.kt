package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.exception.NotSupportFormatException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.key.DensityExtraKey
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.DesktopSvgPainter
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

class DesktopSvgDecoder() : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormatException())
        }

        return runCatching {

            val density = extras[DensityExtraKey]

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
