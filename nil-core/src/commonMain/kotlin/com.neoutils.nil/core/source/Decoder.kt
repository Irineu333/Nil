package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Params
import com.neoutils.nil.core.util.Support
import kotlin.reflect.KClass

val LocalDecoders = compositionLocalOf<List<Decoder<Params>>> { listOf() }

abstract class Decoder<out T : Params>(
    internal val type: KClass<@UnsafeVariance T>
) {

    abstract suspend fun decode(
        input: ByteArray,
        params: @UnsafeVariance T? = null
    ): PainterResource.Result

    abstract suspend fun support(input: ByteArray): Support
}
