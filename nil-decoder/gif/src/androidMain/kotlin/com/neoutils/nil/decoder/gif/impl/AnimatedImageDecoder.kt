package com.neoutils.nil.decoder.gif.impl

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifParam
import com.neoutils.nil.decoder.gif.painter.AnimatedImageGifPainter
import com.neoutils.nil.type.Type

@RequiresApi(Build.VERSION_CODES.P)
class AnimatedImageDecoder : Decoder<GifParam> {

    override val paramType = GifParam::class

    override suspend fun decode(
        input: ByteArray,
        param: GifParam?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            val drawable = createAnimatedImage(input).apply {
                repeatCount = param?.repeatCount ?: Int.MAX_VALUE
            }

            AnimatedImageGifPainter(drawable)
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

    private fun createAnimatedImage(input: ByteArray): AnimatedImageDrawable {

        val drawable = AnimatedImageDrawable
            .createFromStream(input.inputStream(), null)

        return drawable as AnimatedImageDrawable
    }
}