package com.neoutils.nil.core.scope

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Extras

class SettingsScope internal constructor(
    var decoders: List<Decoder>,
    var fetchers: List<Fetcher<*>>,
    var interceptors: List<Interceptor>,
    val extras: Extras.Builder
) {
    fun decoders(vararg decoders: Decoder) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<*>) {
        this.fetchers += fetchers
    }

    fun interceptors(vararg interceptors: Interceptor) {
        this.interceptors += interceptors
    }

    fun decoders(scope: @NilDsl ListScope<Decoder>.() -> Unit) {
        decoders = ListScope
            .from(decoders)
            .apply(scope)
            .get()
    }

    fun fetchers(scope: @NilDsl ListScope<Fetcher<*>>.() -> Unit) {
        fetchers = ListScope
            .from(fetchers)
            .apply(scope)
            .get()
    }


    fun interceptors(scope: @NilDsl ListScope<Interceptor>.() -> Unit) {
        interceptors = ListScope
            .from(interceptors)
            .apply(scope)
            .get()
    }

    fun extras(scope: @NilDsl ExtrasScope.() -> Unit) {
        ExtrasScope(extras).scope()
    }

    internal fun build() = Settings(
        decoders = decoders,
        fetchers = fetchers,
        interceptors = interceptors,
        extras = extras.build()
    )
}
