package com.neoutils.nil.core.decoder

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

interface Decoder {
    suspend fun decode(input: ByteArray): Resource.Result<NilPainter>
    suspend fun support(input: ByteArray): Support
}
