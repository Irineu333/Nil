package com.neoutils.nil.core.scope

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Extras

typealias InterceptorsScope = ListScope<Interceptor>

class SettingsScope internal constructor(
    var interceptors: List<Interceptor>,
    val extras: Extras.Builder
) {
    fun interceptors(vararg interceptors: Interceptor) {
        this.interceptors += interceptors
    }

    fun interceptors(scope: @NilDsl InterceptorsScope.() -> Unit) {
        interceptors = ListScope
            .from(interceptors)
            .apply(scope)
            .get()
    }

    internal fun build() = Settings(
        interceptors = interceptors,
        extras = extras.build()
    )
}
