package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.core.util.Support
import kotlin.reflect.KClass

val LocalDecoders = compositionLocalOf<List<Decoder<Extra>>> { listOf() }

abstract class Decoder<out T : Extra>(
    internal val type: KClass<@UnsafeVariance T>
) {

    abstract suspend fun decode(
        input: ByteArray,
        extra: @UnsafeVariance T? = null
    ): PainterResource.Result

    abstract suspend fun support(input: ByteArray): Support
}
