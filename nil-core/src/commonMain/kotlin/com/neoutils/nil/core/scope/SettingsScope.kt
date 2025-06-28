package com.neoutils.nil.core.scope

import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.source.Interceptor

class SettingsScope internal constructor(
    var decoders: List<Decoder>,
    var fetchers: List<Fetcher<*>>,
    var interceptors: List<Interceptor>,
    val extras: Extras.Builder = Extras.Builder()
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

    fun decoders(scope: AddictionScope<Decoder>.() -> Unit) {
        decoders += AddictionScope<Decoder>().apply(scope).build()
    }

    fun fetchers(scope: AddictionScope<Fetcher<*>>.() -> Unit) {
        fetchers += AddictionScope<Fetcher<*>>().apply(scope).build()
    }

    fun interceptors(scope: AddictionScope<Interceptor>.() -> Unit) {
        interceptors += AddictionScope<Interceptor>().apply(scope).build()
    }

    fun extras(scope: ExtrasScope.() -> Unit) {
        ExtrasScope(extras).apply(scope)
    }

    internal fun build() = Settings(
        decoders = decoders,
        fetchers = fetchers,
        interceptors = interceptors,
        extras = extras.build()
    )
}
