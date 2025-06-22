package com.neoutils.nil.decoder.bitmap.impl

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.type.Type
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder<Extra> {

    override val extraType = Extra::class

    override suspend fun decode(
        input: ByteArray,
        extra: Extra?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            BitmapPainter(input.decodeToImageBitmap())
        }.toPainterResource()
    }

    override suspend fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.PNG -> Support.TOTAL
            Type.JPEG -> Support.TOTAL
            Type.WEBP -> Support.TOTAL
            Type.GIF -> Support.PARTIAL
            else -> Support.NONE
        }
    }
}

