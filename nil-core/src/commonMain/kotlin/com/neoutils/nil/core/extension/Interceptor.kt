package com.neoutils.nil.core.extension

import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import kotlinx.coroutines.flow.*

fun Interceptor.resolve(
    settings: Settings,
    chain: Chain
): Flow<Chain> {
    return flow {
        when (val result = intercept(settings, chain)) {
            is ChainResult.Process -> emitAll(result.flow)
            is ChainResult.Skip -> emit(chain)
        }
    }
}
