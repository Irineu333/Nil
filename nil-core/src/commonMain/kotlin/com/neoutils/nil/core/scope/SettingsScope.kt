package com.neoutils.nil.core.scope

import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher

class SettingsScope internal constructor(
    var decoders: List<Decoder<*>>,
    var fetchers: List<Fetcher<*>>,
    val extras: Extras.Builder = Extras.Builder()
) {
    fun decoders(vararg decoders: Decoder<*>) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<*>) {
        this.fetchers += fetchers
    }

    fun decoders(scope: AddictionScope<Decoder<*>>.() -> Unit) {
        decoders += AddictionScope<Decoder<*>>().apply(scope).build()
    }

    fun fetchers(scope: AddictionScope<Fetcher<*>>.() -> Unit) {
        fetchers += AddictionScope<Fetcher<*>>().apply(scope).build()
    }

    fun extras(scope: ExtrasScope.() -> Unit) {
        extras.add(ExtrasScope().apply(scope).build())
    }

    internal fun build() = Settings(
        decoders = decoders,
        fetchers = fetchers,
        extras = extras.build()
    )
}
