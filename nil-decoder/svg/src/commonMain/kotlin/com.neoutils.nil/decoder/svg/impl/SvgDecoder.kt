package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

expect class SvgDecoder() : Decoder {
    override suspend fun decode(input: ByteArray): Resource.Result<NilPainter>
    override suspend fun support(input: ByteArray): Support
}
