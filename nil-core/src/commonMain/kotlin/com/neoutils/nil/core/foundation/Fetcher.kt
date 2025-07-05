package com.neoutils.nil.core.foundation

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

val LocalFetchers = compositionLocalOf { Dynamic.fetchers }

abstract class Fetcher<out T : Request>(
    internal val type: KClass<@UnsafeVariance T>
) {
    abstract fun fetch(
        input: @UnsafeVariance T,
        extras: Extras = Extras.EMPTY
    ): Flow<Resource<ByteArray>>
}
