package com.neoutils.nil.decoder.svg.impl

import com.caverock.androidsvg.SVG
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.NilSvgComposePainter

actual class SvgDecoder : Decoder {

    actual override suspend fun decode(input: ByteArray): Resource.Result<NilPainter> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportException())
        }

        return runCatching {
            NilSvgComposePainter(SVG.getFromInputStream(input.inputStream()))
        }.toResource()
    }

    actual override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}
