package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.painter.SkiaGifPainter
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class SkiaGifDecoder : Decoder<GifParams> {

    override val extraType = GifParams::class

    override suspend fun decode(
        input: ByteArray,
        extra: GifParams?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            val data = Data.Companion.makeFromBytes(input)
            val codec = Codec.Companion.makeFromData(data)

            SkiaGifPainter(
                codec = codec,
                repeatCount = extra?.repeatCount ?: Int.MAX_VALUE
            )
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when {
            input.startsWith(GIF87A_SPEC) -> Support.TOTAL
            input.startsWith(GIF89A_SPEC) -> Support.TOTAL
            else -> Support.NONE
        }
    }
}