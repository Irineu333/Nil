package com.neoutils.nil.core.extension

import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.ChainResult
import com.neoutils.nil.core.model.Settings
import kotlinx.coroutines.flow.*

fun Interceptor.async(
    settings: Settings,
    chain: Chain.Async
): Flow<Chain.Async> {
    return flow {
        when (val result = intercept(settings, chain)) {
            is ChainResult.Process -> emitAll(result.flow.asAsync())
            is ChainResult.Skip -> emit(chain)
        }
    }
}

fun Flow<Chain>.asAsync(): Flow<Chain.Async> = map { chain -> chain as Chain.Async }

suspend fun Interceptor.sync(
    settings: Settings,
    chain: Chain.Sync
): Chain.Sync {
    return when (val result = intercept(settings, chain)) {
        is ChainResult.Process -> result.flow.single() as Chain.Sync
        is ChainResult.Skip -> chain
    }
}
