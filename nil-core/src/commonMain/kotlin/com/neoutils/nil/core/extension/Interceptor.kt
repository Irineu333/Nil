package com.neoutils.nil.core.extension

import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.flow.*
import kotlin.collections.flatMap

fun Interceptor.resolve(
    extras: Extras,
    chain: Chain
): Flow<Chain> {
    return flow {
        when (val result = intercept(extras, chain)) {
            is ChainResult.Stream -> emitAll(result.chain)
            is ChainResult.Single -> emit(result.chain)
        }
    }
}

fun List<Interceptor>.leveled(): List<Interceptor> {
    return Level.entries.flatMap { level ->
        filter {
            it.levels.contains(level)
        }
    }
}