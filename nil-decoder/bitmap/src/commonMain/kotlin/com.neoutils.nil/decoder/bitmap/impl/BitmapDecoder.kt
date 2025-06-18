package com.neoutils.nil.decoder.bitmap.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.bitmap.format.ImageFormat
import com.neoutils.nil.decoder.bitmap.provider.BitmapPainterProvider
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder {

    override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        return BitmapPainterProvider(input.decodeToImageBitmap())
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
