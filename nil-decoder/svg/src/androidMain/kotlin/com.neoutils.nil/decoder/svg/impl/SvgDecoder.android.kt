package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support

actual class SvgDecoder : Decoder {

    actual override fun decode(input: ByteArray): PainterProvider {
        TODO("Not yet implemented")
    }

    actual override fun support(input: ByteArray): Support {
        return Support.NONE
    }
}