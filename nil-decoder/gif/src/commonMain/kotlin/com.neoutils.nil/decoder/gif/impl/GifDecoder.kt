package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

expect class GifDecoder() : Decoder {
    override suspend fun decode(input: ByteArray): Resource.Result<PainterProvider>
    override fun support(input: ByteArray): Support
}
