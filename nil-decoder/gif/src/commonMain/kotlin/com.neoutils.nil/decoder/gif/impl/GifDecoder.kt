package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support

expect class GifDecoder() : Decoder {
    override fun decode(input: ByteArray): PainterProvider
    override fun support(input: ByteArray): Support
}
