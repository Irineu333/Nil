package com.neoutils.nil.core.scope

import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Extra

class SettingsScope internal constructor(
    var decoders: List<Decoder<Extra>>,
    var fetchers: List<Fetcher<Input>>,
    var extras: List<Extra> = listOf()
) {
    fun decoders(vararg decoders: Decoder<Extra>) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<Input>) {
        this.fetchers += fetchers
    }

    fun extras(vararg extras: Extra) {
        this.extras += extras
    }

    fun decoders(scope: AddictionScope<Decoder<Extra>>.() -> Unit) {
        decoders += AddictionScope(decoders).apply(scope).values
    }

    fun fetchers(scope: AddictionScope<Fetcher<Input>>.() -> Unit) {
        fetchers += AddictionScope(fetchers).apply(scope).values
    }

    fun extras(scope: AddictionScope<Extra>.() -> Unit) {
        extras += AddictionScope(extras).apply(scope).values
    }

    internal fun build() = Settings(
        decoders = decoders,
        fetchers = fetchers,
        params = extras
    )
}
