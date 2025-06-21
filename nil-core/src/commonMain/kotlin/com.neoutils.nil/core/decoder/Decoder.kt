package com.neoutils.nil.core.decoder

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

interface Decoder {
    suspend fun decode(input: ByteArray): PainterResource.Result
    suspend fun support(input: ByteArray): Support
}
