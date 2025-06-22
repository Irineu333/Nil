package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifParams

expect class GifDecoder() : Decoder<GifParams> {

    override suspend fun decode(
        input: ByteArray,
        extra: GifParams?
    ): PainterResource.Result

    override suspend fun support(input: ByteArray): Support
}
