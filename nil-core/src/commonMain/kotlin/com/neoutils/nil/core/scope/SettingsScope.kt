package com.neoutils.nil.core.scope

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor2
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Extras

typealias FetchersScope = ListScope<Fetcher<*>>
typealias DecodersScope = ListScope<Decoder>
typealias InterceptorsScope = ListScope<Interceptor2>

class SettingsScope internal constructor(
    var decoders: List<Decoder>,
    var fetchers: List<Fetcher<*>>,
    var interceptors: List<Interceptor2>,
    val extras: Extras.Builder
) {
    fun decoders(vararg decoders: Decoder) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<*>) {
        this.fetchers += fetchers
    }

    fun interceptors(vararg interceptors: Interceptor2) {
        this.interceptors += interceptors
    }

    fun decoders(scope: @NilDsl DecodersScope.() -> Unit) {
        decoders = ListScope
            .from(decoders)
            .apply(scope)
            .get()
    }

    fun fetchers(scope: @NilDsl FetchersScope.() -> Unit) {
        fetchers = ListScope
            .from(fetchers)
            .apply(scope)
            .get()
    }

    fun interceptors(scope: @NilDsl InterceptorsScope.() -> Unit) {
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
