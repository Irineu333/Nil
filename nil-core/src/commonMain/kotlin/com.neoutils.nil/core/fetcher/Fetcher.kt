package com.neoutils.nil.core.fetcher

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

val LocalFetchers = compositionLocalOf<List<Fetcher<Input>>> { listOf() }

abstract class Fetcher<out T : Input>(val type: KClass<@UnsafeVariance T>) {

    abstract suspend fun get(input: @UnsafeVariance T): Resource.Result<ByteArray>

    abstract fun fetch(input: @UnsafeVariance T): Flow<Resource<ByteArray>>
}
