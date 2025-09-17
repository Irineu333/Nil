package com.neoutils.nil.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.foundation.LocalInterceptors
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.LocalExtras

val CompositeKeyHash = Extras.Key<Int>()

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
    LocalExtras provides settings.extras,
    LocalInterceptors provides settings.interceptors,
    content = content
)

@Composable
fun rememberSettings(block: @NilDsl SettingsScope.() -> Unit): Settings {

    val interceptors = LocalInterceptors.current
    val extras = LocalExtras.current

    val density = LocalDensity.current
    val keyHash = currentCompositeKeyHash

    val scope = remember(
        interceptors,
        extras
    ) {
        SettingsScope(
            interceptors = interceptors,
            extras = extras.newBuilder().apply {
                set(DensityExtrasKey, density)
                set(CompositeKeyHash, keyHash)
            }
        )
    }

    return remember(scope) { scope.apply(block).build() }
}
