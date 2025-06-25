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

    override val paramsKey = GifParams.ExtraKey

    override suspend fun decode(
        input: ByteArray,
        params: GifParams
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            MovieGifPainter(
                movie = Movie.decodeStream(input.inputStream()),
                repeatCount = params.repeatCount
            )
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.TOTAL
            else -> Support.NONE
        }
    }
}