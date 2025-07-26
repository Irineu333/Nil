@file:OptIn(ExperimentalStdlibApi::class)

package com.neoutils.nil.interceptor.diskCache

import com.neoutils.nil.core.util.interceptors
import com.neoutils.nil.interceptor.diskcache.impl.DiskCacheInterceptor

@EagerInitialization
private val initHook = {
    interceptors.add(DiskCacheInterceptor())
}.invoke()