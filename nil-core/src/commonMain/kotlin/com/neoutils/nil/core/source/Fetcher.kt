package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

val LocalFetchers = compositionLocalOf<List<Fetcher<Request>>> { listOf() }

abstract class Fetcher<out T : Request>(
    internal val type: KClass<@UnsafeVariance T>
) {
    abstract fun fetch(
        input: @UnsafeVariance T,
        extras: Extras = Extras.EMPTY
    ): Flow<Resource<ByteArray>>
}
