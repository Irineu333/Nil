package com.neoutils.nil.core.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

val DefaultInterceptors = listOf(
    FetchInterceptor(),
    DecodeInterceptor()
)

val LocalInterceptors = staticCompositionLocalOf {
    DefaultInterceptors + Dynamic.interceptors
}

abstract class Interceptor2(vararg val levels: Level) {

    abstract fun intercept(
        settings: Settings,
        chain: Chain,
    ): ChainResult

    fun async(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {
        return when (val result = intercept(settings, chain)) {
            is ChainResult.Process -> result.async
            is ChainResult.Skip -> flowOf(chain)
        }
    }

    suspend fun sync(
        settings: Settings,
        chain: Chain
    ): Chain {
        return when (val result = intercept(settings, chain)) {
            is ChainResult.Process -> result.sync()
            is ChainResult.Skip -> chain
        }
    }
}

typealias SyncChain = suspend () -> Chain

sealed class ChainResult {

    data class Process(
        val async: Flow<Chain>,
        val sync: SyncChain,
    ): ChainResult() {
        constructor(chain: suspend () -> Chain) : this(
            async = flow { emit(chain()) },
            sync = chain,
        )
    }

    data object Skip: ChainResult()
}

