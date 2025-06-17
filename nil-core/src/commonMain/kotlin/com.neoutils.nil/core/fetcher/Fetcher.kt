package com.neoutils.nil.core.fetcher

import androidx.compose.runtime.Composable
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

@Composable
fun rememberTargetFetcher(
    inputClass: KClass<out Input>,
    fetchers: List<Fetcher<Input>> = LocalFetchers.current
): Fetcher<Input> {

    val fetcher = fetchers.find { it.type == inputClass }

    return checkNotNull(fetcher) { "No supported fetcher found" }
}