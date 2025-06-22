package com.neoutils.nil.decoder.gif.impl

import android.graphics.Movie
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.painter.AnimatedImageGifPainter
import com.neoutils.nil.decoder.gif.painter.MovieGifPainter

class AndroidGifDecoder : Decoder<GifParams> {

    override val extraType = GifParams::class

    override suspend fun decode(
        input: ByteArray,
        extra: GifParams?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                    val drawable = AnimatedImageDrawable
                        .createFromStream(input.inputStream(), null)
                            as AnimatedImageDrawable

                    drawable.repeatCount = extra?.repeatCount ?: Int.MAX_VALUE

                    AnimatedImageGifPainter(drawable)
                }

                else -> @Suppress("DEPRECATION") {
                    MovieGifPainter(
                        movie = Movie.decodeStream(input.inputStream()),
                        repeatCount = extra?.repeatCount ?: Int.MAX_VALUE
                    )
                }
            }
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