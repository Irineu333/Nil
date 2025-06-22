package com.neoutils.nil.decoder.gif.impl

import android.graphics.Movie
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.painter.MovieGifPainter
import com.neoutils.nil.type.Type

@Suppress("DEPRECATION")
class MovieGifDecoder : Decoder<GifParams> {

    override val extraType = GifParams::class

    override suspend fun decode(
        input: ByteArray,
        extra: GifParams?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            MovieGifPainter(
                movie = Movie.decodeStream(input.inputStream()),
                repeatCount = extra?.repeatCount ?: Int.MAX_VALUE
            )
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.Companion.detect(input)) {
            Type.GIF -> Support.TOTAL
            else -> Support.NONE
        }
    }
}