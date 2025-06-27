package com.neoutils.nil.core.source

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings

val LocalInterceptors = compositionLocalOf {
    listOf(
        FetchInterceptor(),
        DecodeInterceptor()
    )
}

interface Interceptor {
    suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Chain
}