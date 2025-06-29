package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.exception.NotSupportFormatException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.painter.SkiaGifPainter
import com.neoutils.nil.type.Type
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class SkiaGifDecoder : Decoder {

    private val cache = mutableMapOf<ByteArray, Codec>()

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormatException())
        }

        return runCatching {
            val params = extras[GifParams.ExtrasKey]

            val codec = cache.getOrElse(input) {
                Codec.makeFromData(Data.makeFromBytes(input))
            }

            SkiaGifPainter(
                codec = codec,
                repeatCount = params.repeatCount
            )
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.RECOMMEND
            Type.WEBP if input.isAnimated() -> Support.RECOMMEND
            Type.WEBP, Type.PNG, Type.JPEG -> Support.SUPPORT
            else -> Support.NONE
        }
    }

    fun ByteArray.isAnimated() = runCatching {

        val codec = cache.getOrElse(this) {
            Codec.makeFromData(Data.makeFromBytes(this))
        }

        codec.framesInfo.isNotEmpty()
    }.getOrElse {
        false
    }
}