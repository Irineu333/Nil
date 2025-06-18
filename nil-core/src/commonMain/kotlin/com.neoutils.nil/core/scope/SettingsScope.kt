package com.neoutils.nil.core.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.decoder.LocalDecoders
import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.fetcher.LocalFetchers
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Input

class SettingsScope internal constructor(
    var decoders: List<Decoder>,
    var fetchers: List<Fetcher<Input>>
) {
    fun decoders(vararg decoders: Decoder) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<Input>) {
        this.fetchers += fetchers
    }

    fun decoders(block: SettingScope<Decoder>.() -> Unit) {
        decoders += SettingScope(decoders).apply(block).values
    }

    fun fetchers(block: SettingScope<Fetcher<Input>>.() -> Unit) {
        fetchers += SettingScope(fetchers).apply(block).values
    }

    internal fun build() = Settings(
        decoders = decoders.toList(),
        fetchers = fetchers.toList()
    )
}

class SettingScope<T>(
    internal var values: List<T>
) {
    fun add(value: T) {
        values += value
    }
}

@Composable
fun ProvideSettings(
    settings: Settings,
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalDecoders provides settings.decoders,
    LocalFetchers provides settings.fetchers,
    content = content
)

@Composable
fun rememberSettings(block: SettingsScope.() -> Unit): Settings {

    val decoders = LocalDecoders.current
    val fetchers = LocalFetchers.current

    val scope = remember(decoders, fetchers) {
        SettingsScope(
            decoders = decoders.toMutableList(),
            fetchers = fetchers.toMutableList()
        )
    }

    return remember(scope, block) { scope.apply(block).build() }
}