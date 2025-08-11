package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.core.scope.InterceptorsScope
import com.neoutils.nil.interceptor.diskcache.impl.DiskCacheInterceptor

fun InterceptorsScope.diskCache() {
    add(DiskCacheInterceptor())
}
