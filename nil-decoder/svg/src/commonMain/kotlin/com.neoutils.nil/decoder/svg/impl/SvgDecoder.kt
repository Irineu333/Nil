package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support

expect class SvgDecoder() : Decoder {
    override fun decode(input: ByteArray): PainterProvider
    override fun support(input: ByteArray): Support
}
