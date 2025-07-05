package com.neoutils.nil.core.foundation

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.flow.Flow

val DefaultInterceptors = listOf(
    FetchInterceptor(),
    DecodeInterceptor()
)

val LocalInterceptors = compositionLocalOf {
    DefaultInterceptors + Dynamic.interceptors
}

abstract class Interceptor(vararg val levels: Level) {
    abstract fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain>
}
