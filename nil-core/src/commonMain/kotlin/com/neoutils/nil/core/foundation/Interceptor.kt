package com.neoutils.nil.core.foundation

import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level

val DefaultInterceptors = listOf(
    FetchInterceptor(),
    DecodeInterceptor()
)

val InterceptorsExtras = Extras.Key(DefaultInterceptors + Dynamic.interceptors)

abstract class Interceptor(vararg val levels: Level) {
    abstract suspend fun intercept(
        extras: Extras,
        chain: Chain,
    ): ChainResult
}
