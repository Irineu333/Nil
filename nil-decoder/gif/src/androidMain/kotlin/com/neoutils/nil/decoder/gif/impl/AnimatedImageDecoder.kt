package com.neoutils.nil.decoder.gif.impl

import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.neoutils.nil.core.exception.NotSupportFormatException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifParams
import com.neoutils.nil.decoder.gif.painter.AnimatedImageGifPainter
import com.neoutils.nil.decoder.gif.painter.DrawablePainter
import com.neoutils.nil.type.Type

@RequiresApi(Build.VERSION_CODES.P)
class AnimatedImageDecoder : Decoder {

    private val cache = mutableMapOf<ByteArray, Drawable>()

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormatException())
        }

        return runCatching {
            val params = extras[GifParams.ExtraKey]

            when (val drawable = input.toDrawable()) {
                is AnimatedImageDrawable -> {
                    drawable.repeatCount = params.repeatCount

                    AnimatedImageGifPainter(drawable)
                }

                else -> {
                    DrawablePainter(drawable)
                }
            }
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

    private fun ByteArray.toDrawable(): Drawable {

        val drawable = cache.getOrPut(this) {
            checkNotNull(
                AnimatedImageDrawable.createFromStream(inputStream(), null)
            )
        }

        return drawable
    }

    private fun ByteArray.isAnimated() = runCatching {
        toDrawable() is AnimatedImageDrawable
    }.getOrElse {
        false
    }
}