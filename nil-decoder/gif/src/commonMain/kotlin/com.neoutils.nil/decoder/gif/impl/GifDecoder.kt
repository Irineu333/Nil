package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Params
import com.neoutils.nil.core.util.Support

expect class GifDecoder() : Decoder<Params> {

    override suspend fun decode(
        input: ByteArray,
        params: Params?
    ): PainterResource.Result

    override suspend fun support(input: ByteArray): Support
}
