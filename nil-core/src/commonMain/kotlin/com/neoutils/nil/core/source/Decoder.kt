package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

interface Decoder {
    suspend fun decode(
        input: ByteArray,
        extras: Extras = Extras.EMPTY
    ): PainterResource.Result

    suspend fun support(input: ByteArray): Support
}
