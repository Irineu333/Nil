package com.neoutils.nil.core.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

val DefaultInterceptors = listOf(
    FetchInterceptor(),
    DecodeInterceptor()
)

val LocalInterceptors = staticCompositionLocalOf {
    DefaultInterceptors + Dynamic.interceptors
}

abstract class Interceptor3(vararg val levels: Level) {

    abstract suspend fun intercept(
        settings: Settings,
        chain: Chain,
    ): Chain.Result
}

fun Interceptor3.async(
    settings: Settings,
    chain: Chain
): Flow<Chain> {
    return flow {
        when (val result = intercept(settings, chain)) {
            is Chain.Result.Sync -> emit(result.chain)
            is Chain.Result.Async -> emitAll(result.flow)
            is Chain.Result.Skip -> emit(chain)
        }
    }
}

suspend fun Interceptor3.sync(
    settings: Settings,
    chain: Chain.Sync
): Chain.Sync {
    return when (val result = intercept(settings, chain)) {
        is Chain.Result.Skip -> chain
        is Chain.Result.Sync if result.chain is Chain.Sync -> result.chain
        else -> error("Don't support async")
    }
}
