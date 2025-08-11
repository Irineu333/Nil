package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.interceptor.diskcache.model.DiskCacheExtra

fun ExtrasScope.diskCache(
    scope: @NilDsl DiskCacheExtra.Builder.() -> Unit
) {
    extras.update(DiskCacheExtra.ExtrasKey) {
        it.newBuilder()
            .apply(scope)
            .build()
    }
}
