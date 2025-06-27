package com.neoutils.nil.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.key.DensityExtraKey
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.LocalExtras
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.source.LocalDecoders
import com.neoutils.nil.core.source.LocalFetchers

@Composable
fun ProvideSettings(
    settings: Settings,
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalDecoders provides settings.decoders,
    LocalFetchers provides settings.fetchers,
    LocalExtras provides settings.extras,
    content = content
)

@Composable
fun rememberSettings(block: SettingsScope.() -> Unit): Settings {

    val decoders = LocalDecoders.current
    val fetchers = LocalFetchers.current
    val extras = LocalExtras.current

    val density = LocalDensity.current

    val scope = remember(decoders, fetchers) {
        SettingsScope(
            decoders = decoders,
            fetchers = fetchers,
            extras = extras.newBuilder().apply {
                set(DensityExtraKey, density)
            }
        )
    }

    return remember(scope, block) { scope.apply(block).build() }
}