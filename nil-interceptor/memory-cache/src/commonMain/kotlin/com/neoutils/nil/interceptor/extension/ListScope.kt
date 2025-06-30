package com.neoutils.nil.interceptor.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.interceptor.memoryCache.MemoryCacheInterceptor

fun ListScope<Interceptor>.memoryCache() {
    add(MemoryCacheInterceptor())
}
