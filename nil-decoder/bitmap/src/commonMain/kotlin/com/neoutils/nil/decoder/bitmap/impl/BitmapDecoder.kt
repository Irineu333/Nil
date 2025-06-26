package com.neoutils.nil.decoder.bitmap.impl

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.neoutils.nil.core.exception.NotSupportFormatException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.type.Type
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormatException())
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
            Type.WEBP -> Support.PARTIAL
            Type.GIF -> Support.PARTIAL
            else -> Support.NONE
        }
    }
}

