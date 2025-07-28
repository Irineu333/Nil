package com.neoutils.nil.interceptor.memoryCache.extension

import com.neoutils.nil.core.scope.InterceptorsScope
import com.neoutils.nil.interceptor.memoryCache.impl.MemoryCacheInterceptor

fun InterceptorsScope.memoryCache() {
    add(MemoryCacheInterceptor())
}
