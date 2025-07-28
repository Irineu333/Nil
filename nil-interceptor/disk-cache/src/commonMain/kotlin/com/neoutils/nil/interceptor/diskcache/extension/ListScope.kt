package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.foundation.Interceptor3
import com.neoutils.nil.interceptor.diskcache.impl.DiskCacheInterceptor

fun ListScope<Interceptor3>.diskCache() {
    add(DiskCacheInterceptor())
}
