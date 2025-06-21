package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Params
import com.neoutils.nil.core.util.Support

expect class SvgDecoder() : Decoder<Params> {

    override suspend fun decode(
        input: ByteArray,
        params: Params?
    ): PainterResource.Result

    override suspend fun support(input: ByteArray): Support
}
