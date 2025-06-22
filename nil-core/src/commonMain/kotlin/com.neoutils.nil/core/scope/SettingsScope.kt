package com.neoutils.nil.core.scope

import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Extra

class SettingsScope internal constructor(
    var decoders: List<Decoder<Extra>>,
    var fetchers: List<Fetcher<Input>>,
    var extras: List<Extra> = mutableListOf()
) {
    fun decoders(vararg decoders: Decoder<Extra>) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<Input>) {
        this.fetchers += fetchers
    }

    fun decoders(block: AddictionScope<Decoder<Extra>>.() -> Unit) {
        decoders += AddictionScope(decoders).apply(block).values
    }

    fun fetchers(block: AddictionScope<Fetcher<Input>>.() -> Unit) {
        fetchers += AddictionScope(fetchers).apply(block).values
    }

    internal fun build() = Settings(
        decoders = decoders.toList(),
        fetchers = fetchers.toList(),
        params = extras
    )
}
