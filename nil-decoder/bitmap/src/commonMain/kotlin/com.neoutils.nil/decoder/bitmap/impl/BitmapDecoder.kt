package com.neoutils.nil.decoder.bitmap.impl

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.painter.NilStaticPainter
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.bitmap.format.ImageFormat
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder {

    override suspend fun decode(input: ByteArray): Resource.Result<NilPainter> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportException())
        }

        return runCatching {
            NilStaticPainter(BitmapPainter(input.decodeToImageBitmap()))
        }.toResource()
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

