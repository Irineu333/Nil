package com.neoutils.nil.decoder.gif.impl

import android.graphics.Movie
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.painter.NilFlowAnimationPainter
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.painter.NilGifPainterApi28
import com.neoutils.nil.decoder.gif.painter.NilGifPainterLowerApi

actual class GifDecoder : Decoder {
    actual override suspend fun decode(input: ByteArray): Resource.Result<NilFlowAnimationPainter> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportException())
        }

        return runCatching {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                    NilGifPainterApi28(
                        drawable = AnimatedImageDrawable.createFromStream(
                            input.inputStream(), null
                        ) as AnimatedImageDrawable
                    )
                }

                else -> @Suppress("DEPRECATION") {
                    NilGifPainterLowerApi(
                        movie = Movie.decodeStream(input.inputStream())
                    )
                }
            }
        }.toResource()
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
