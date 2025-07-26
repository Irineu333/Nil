@file:OptIn(ExperimentalStdlibApi::class)

package com.neoutils.nil.interceptor.memoryCache

import com.neoutils.nil.core.util.interceptors
import com.neoutils.nil.interceptor.memoryCache.impl.MemoryCacheInterceptor

@EagerInitialization
private val initHook = run {
    interceptors.add(MemoryCacheInterceptor())
}
