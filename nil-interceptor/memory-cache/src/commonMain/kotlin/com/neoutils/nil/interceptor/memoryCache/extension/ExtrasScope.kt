package com.neoutils.nil.interceptor.memoryCache.extension

import com.neoutils.nil.core.annotation.SettingsDsl
import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra

fun ExtrasScope.memoryCache(
    scope: @SettingsDsl MemoryCacheExtra.Builder.() -> Unit
) {
    extras.update(MemoryCacheExtra.ExtrasKey) {
        it.newBuilder().apply(scope).build()
    }
}