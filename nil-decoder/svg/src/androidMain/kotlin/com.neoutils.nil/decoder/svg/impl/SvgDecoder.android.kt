package com.neoutils.nil.decoder.svg.impl

import com.caverock.androidsvg.SVG
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.SvgDelegatePainter

actual class SvgDecoder : Decoder {

    actual override suspend fun decode(input: ByteArray): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            SvgDelegatePainter(SVG.getFromInputStream(input.inputStream()))
        }.toPainterResource()
    }

    actual override suspend fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}
