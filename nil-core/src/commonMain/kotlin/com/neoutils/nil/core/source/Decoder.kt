package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf<List<Decoder<*>>> { listOf() }

interface Decoder<out T> {

    val paramsKey: Extras.Key<T>

    suspend fun decode(
        input: ByteArray,
        params: @UnsafeVariance T
    ): PainterResource.Result

    suspend fun support(input: ByteArray): Support
}
