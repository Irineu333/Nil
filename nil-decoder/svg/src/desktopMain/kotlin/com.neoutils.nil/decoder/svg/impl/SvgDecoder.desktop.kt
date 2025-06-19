package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.provider.SvgPainterProvider

actual class SvgDecoder : Decoder {

    actual override suspend fun decode(input: ByteArray): Resource.Result<PainterProvider> {

        return when (support(input)) {
            Support.NONE -> Resource.Result.Failure(NotSupportException())
            else -> Resource.Result.Success(SvgPainterProvider(input))
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
