package com.neoutils.nil.interceptor.memoryCache.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.foundation.Interceptor2
import com.neoutils.nil.interceptor.memoryCache.impl.MemoryCacheInterceptor

fun ListScope<Interceptor2>.memoryCache() {
    add(MemoryCacheInterceptor())
}
