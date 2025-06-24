package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.Param
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import kotlin.reflect.KClass

val LocalDecoders = compositionLocalOf<List<Decoder<Param>>> { listOf() }

interface Decoder<out T : Param> {

    val paramType: KClass<@UnsafeVariance T>

    suspend fun decode(
        input: ByteArray,
        param: @UnsafeVariance T? = null
    ): PainterResource.Result

    suspend fun support(input: ByteArray): Support
}
