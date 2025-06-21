package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.painter.GifPainterSkia
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

actual class GifDecoder : Decoder {
    actual override suspend fun decode(input: ByteArray): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            val data = Data.makeFromBytes(input)
            val codec = Codec.makeFromData(data)
            GifPainterSkia(codec)
        }.toPainterResource()
    }

    actual override suspend fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE

        return when {
            input.startsWith(GIF87A_SPEC) -> Support.TOTAL
            input.startsWith(GIF89A_SPEC) -> Support.TOTAL
            else -> Support.NONE
        }
    }
}
