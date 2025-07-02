package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.interceptor.diskcache.impl.DiskCacheInterceptor

fun ListScope<Interceptor>.diskCache() {
    add(DiskCacheInterceptor())
}
