package com.neoutils.nil.core.scope

import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Params

class SettingsScope internal constructor(
    var decoders: List<Decoder<Params>>,
    var fetchers: List<Fetcher<Input>>,
    var params: List<Params> = mutableListOf()
) {
    fun decoders(vararg decoders: Decoder<Params>) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<Input>) {
        this.fetchers += fetchers
    }

    fun decoders(block: AddictionScope<Decoder<Params>>.() -> Unit) {
        decoders += AddictionScope(decoders).apply(block).values
    }

    fun fetchers(block: AddictionScope<Fetcher<Input>>.() -> Unit) {
        fetchers += AddictionScope(fetchers).apply(block).values
    }

    internal fun build() = Settings(
        decoders = decoders.toList(),
        fetchers = fetchers.toList(),
        params = params
    )
}
