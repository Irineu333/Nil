package com.neoutils.nil.decoder.bitmap.impl

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.bitmap.format.ImageFormat
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

        return when (ImageFormat.detect(input)) {
            ImageFormat.PNG -> Support.TOTAL
            ImageFormat.JPEG -> Support.TOTAL
            ImageFormat.WEBP -> Support.TOTAL
            ImageFormat.GIF -> Support.PARTIAL
            null -> Support.NONE
        }
    }
}

