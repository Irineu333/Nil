package image.core.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import image.core.decoder.Decoder
import image.core.decoder.LocalDecoders
import image.core.fetcher.Fetcher
import image.core.fetcher.LocalFetchers
import image.core.model.Settings
import image.core.util.Input

class SettingsScope internal constructor(
    var decoders: MutableList<Decoder>,
    var fetchers: MutableList<Fetcher<Input>>
) {
    fun decoders(vararg decoders: Decoder) {
        this.decoders.addAll(decoders.toList())
    }

    fun fetchers(vararg fetchers: Fetcher<Input>) {
        this.fetchers.addAll(fetchers.toList())
    }

    fun decoders(block: DecodersScope.() -> Unit) {
        DecodersScope(decoders).apply(block)
    }

    fun fetchers(block: FetchersScope.() -> Unit) {
        FetchersScope(fetchers).apply(block)
    }

    internal fun build() = Settings(
        decoders = decoders.toList(),
        fetchers = fetchers.toList()
    )
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