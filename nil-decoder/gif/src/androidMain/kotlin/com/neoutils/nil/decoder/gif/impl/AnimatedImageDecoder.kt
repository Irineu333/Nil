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
import com.neoutils.nil.type.Type

@RequiresApi(Build.VERSION_CODES.P)
class AnimatedImageDecoder : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormatException())
        }

        return runCatching {
            val params = extras[GifParams.ExtraKey]

            val drawable = createAnimatedImage(input)

            when (drawable) {
                is AnimatedImageDrawable -> {
                    drawable.repeatCount = params.repeatCount

                    AnimatedImageGifPainter(drawable)
                }

                else -> TODO("Implement static image support")
            }
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.RECOMMEND
            Type.WEBP if input.isAnimated() -> Support.RECOMMEND
            Type.WEBP, Type.PNG, Type.JPEG -> Support.TOTAL
            else -> Support.NONE
        }
    }

    private fun createAnimatedImage(input: ByteArray): Drawable {

        val drawable = AnimatedImageDrawable.createFromStream(input.inputStream(), null)

        return checkNotNull(drawable)
    }

    fun ByteArray.isAnimated() = runCatching {
        createAnimatedImage(this) is AnimatedImageDrawable
    }.getOrElse {
        false
    }
}