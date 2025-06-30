package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf { Dynamic.decoders }

interface Decoder {
    suspend fun decode(
        input: ByteArray,
        extras: Extras = Extras.EMPTY
    ): PainterResource.Result

    suspend fun support(input: ByteArray): Support
}
