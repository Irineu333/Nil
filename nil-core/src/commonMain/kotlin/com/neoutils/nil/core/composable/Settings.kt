package com.neoutils.nil.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.LocalExtras
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.foundation.LocalDecoders
import com.neoutils.nil.core.foundation.LocalFetchers
import com.neoutils.nil.core.foundation.LocalInterceptors

@Composable
fun ProvideSettings(
    settings: Settings,
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalDecoders provides settings.decoders,
    LocalFetchers provides settings.fetchers,
    LocalExtras provides settings.extras,
    LocalInterceptors provides settings.interceptors,
    content = content
)

@Composable
fun rememberSettings(block: SettingsScope.() -> Unit): Settings {

    val decoders = LocalDecoders.current
    val fetchers = LocalFetchers.current
    val interceptors = LocalInterceptors.current
    val extras = LocalExtras.current

    val density = LocalDensity.current

    val scope = remember(
        decoders,
        fetchers,
        interceptors,
        extras
    ) {
        SettingsScope(
            decoders = decoders,
            fetchers = fetchers,
            interceptors = interceptors,
            extras = extras.newBuilder().apply {
                set(DensityExtrasKey, density)
            }
        )
    }

    return remember(scope, block) { scope.apply(block).build() }
}