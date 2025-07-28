package com.neoutils.nil.decoder.svg.impl

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.extension.resourceCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.SkiaSvgPainter
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

class SkiaSvgDecoder() : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): Resource.Result<Painter> {

        if (support(input) == Support.NONE) {
            return  Resource.Result.Failure(NotSupportFormat())
        }

        return resourceCatching {

            val density = extras[DensityExtrasKey]

            SkiaSvgPainter(SVGDOM(Data.makeFromBytes(input)), density)
        }
    }

    override suspend fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.RECOMMEND
        }

        return Support.NONE
    }
}
