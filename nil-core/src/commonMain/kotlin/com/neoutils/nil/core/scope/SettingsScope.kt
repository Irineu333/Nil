package com.neoutils.nil.core.scope

import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Param

class SettingsScope internal constructor(
    var decoders: List<Decoder<Param>>,
    var fetchers: List<Fetcher<Input>>,
    var params: List<Param> = listOf()
) {
    fun decoders(vararg decoders: Decoder<Param>) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<Input>) {
        this.fetchers += fetchers
    }

    fun extras(vararg params: Param) {
        this.params += params
    }

    fun decoders(scope: AddictionScope<Decoder<Param>>.() -> Unit) {
        decoders = AddictionScope(decoders).apply(scope).values
    }

    fun fetchers(scope: AddictionScope<Fetcher<Input>>.() -> Unit) {
        fetchers = AddictionScope(fetchers).apply(scope).values
    }

    fun extras(scope: AddictionScope<Param>.() -> Unit) {
        params = AddictionScope(params).apply(scope).values
    }

    internal fun build() = Settings(
        decoders = decoders,
        fetchers = fetchers,
        params = params
    )
}
