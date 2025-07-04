package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.core.annotation.SettingsDsl
import com.neoutils.nil.core.scope.ExtrasScope

fun ExtrasScope.diskCache(
    scope: @SettingsDsl DiskCacheExtra.Builder.() -> Unit
) {
    extras.update(DiskCacheExtra.ExtrasKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}
