package com.neoutils.nil.decoder.bitmap.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.bitmap.format.ImageFormat
import com.neoutils.nil.decoder.bitmap.provider.BitmapPainterProvider
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder {

    override suspend fun decode(input: ByteArray): Resource.Result<PainterProvider> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportException())
        }

        return runCatching {
            BitmapPainterProvider(input.decodeToImageBitmap())
        }.toResource()
    }

    override fun support(input: ByteArray): Support {
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
