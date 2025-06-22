package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import kotlin.reflect.KClass

val LocalDecoders = compositionLocalOf<List<Decoder<Extra>>> { listOf() }

interface Decoder<out T : Extra> {

    val extraType: KClass<@UnsafeVariance T>

    suspend fun decode(
        input: ByteArray,
        extra: @UnsafeVariance T? = null
    ): PainterResource.Result

    suspend fun support(input: ByteArray): Support
}
