package com.neoutils.nil.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.foundation.LocalDecoders
import com.neoutils.nil.core.foundation.LocalFetchers
import com.neoutils.nil.core.foundation.LocalInterceptors
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.LocalExtras

@Composable
fun ProvideSettings(
    settings: @NilDsl SettingsScope.() -> Unit,
    content: @Composable () -> Unit
) = ProvideSettings(
    settings = rememberSettings(settings),
    content = content
)

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
fun rememberSettings(block: @NilDsl SettingsScope.() -> Unit): Settings {

    val decoders = LocalDecoders.current
    val fetchers = LocalFetchers.current
    val interceptors = LocalInterceptors.current
    val extras = LocalExtras.current

    val density = LocalDensity.current
    val keyHash = currentCompositeKeyHash

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
                set(CompositeKeyHash, keyHash)
            }
        )
    }

    return remember(scope) { scope.apply(block).build() }
}

val CompositeKeyHash = Extras.Key<Int>()