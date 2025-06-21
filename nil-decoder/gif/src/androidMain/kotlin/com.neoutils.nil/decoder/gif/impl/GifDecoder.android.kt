package com.neoutils.nil.decoder.gif.impl

import android.graphics.Movie
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Params
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.painter.GifPainterApi28
import com.neoutils.nil.decoder.gif.painter.GifPainterLowerApi

actual class GifDecoder : Decoder<Params>(Params::class) {
    actual override suspend fun decode(
        input: ByteArray,
        params: Params?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                    GifPainterApi28(
                        drawable = AnimatedImageDrawable.createFromStream(
                            input.inputStream(), null
                        ) as AnimatedImageDrawable
                    )
                }

                else -> @Suppress("DEPRECATION") {
                    GifPainterLowerApi(
                        movie = Movie.decodeStream(input.inputStream())
                    )
                }
            }
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
