package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support

expect class SvgDecoder() : Decoder {
    override suspend fun decode(input: ByteArray): PainterResource.Result
    override suspend fun support(input: ByteArray): Support
}
