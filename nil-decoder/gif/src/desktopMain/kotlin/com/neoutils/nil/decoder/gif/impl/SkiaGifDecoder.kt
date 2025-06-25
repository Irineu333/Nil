package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.painter.SkiaGifPainter
import com.neoutils.nil.type.Type
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class SkiaGifDecoder : Decoder<GifParams> {

    override val paramsKey = GifParams.ExtraKey

    override suspend fun decode(
        input: ByteArray,
        params: GifParams
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            val data = Data.Companion.makeFromBytes(input)
            val codec = Codec.Companion.makeFromData(data)

            SkiaGifPainter(
                codec = codec,
                repeatCount = params.repeatCount
            )
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.TOTAL
            Type.WEBP -> Support.TOTAL
            else -> Support.NONE
        }
    }
}