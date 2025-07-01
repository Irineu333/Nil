package com.neoutils.nil.core.scope

import com.neoutils.nil.core.annotation.SettingsDsl
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.Extras

@SettingsDsl
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

    fun decoders(scope: ListScope<Decoder>.() -> Unit) {
        decoders = ListScope
            .from(decoders)
            .apply(scope)
            .get()
    }

    fun fetchers(scope: ListScope<Fetcher<*>>.() -> Unit) {
        fetchers = ListScope
            .from(fetchers)
            .apply(scope)
            .get()
    }

    fun interceptors(scope: ListScope<Interceptor>.() -> Unit) {
        interceptors = ListScope
            .from(interceptors)
            .apply(scope)
            .get()
    }

    fun extras(scope: ExtrasScope.() -> Unit) {
        ExtrasScope(extras).scope()
    }

    internal fun build() = Settings(
        decoders = decoders,
        fetchers = fetchers,
        interceptors = interceptors,
        extras = extras.build()
    )
}
