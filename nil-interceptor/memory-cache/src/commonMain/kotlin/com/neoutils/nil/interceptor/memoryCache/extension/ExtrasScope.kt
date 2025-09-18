package com.neoutils.nil.interceptor.memoryCache.extension

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra

fun SettingsScope.memoryCache(
    scope: @NilDsl MemoryCacheExtra.Builder.() -> Unit
) {
    extras.update(MemoryCacheExtra.ExtrasKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}