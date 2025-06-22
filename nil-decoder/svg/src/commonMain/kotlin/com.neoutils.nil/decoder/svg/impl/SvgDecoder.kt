package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.core.util.Support

expect class SvgDecoder() : Decoder<Extra> {

    override suspend fun decode(
        input: ByteArray,
        extra: Extra?
    ): PainterResource.Result

    override suspend fun support(input: ByteArray): Support
}
