package com.neoutils.nil.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.foundation.DecodersExtra
import com.neoutils.nil.core.foundation.FetchersExtra
import com.neoutils.nil.core.foundation.InterceptorsExtras
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.Extras

val CompositeKeyHash = Extras.Key<Int>()

val LocalExtras = staticCompositionLocalOf { Extras.EMPTY }

@Composable
fun ProvideExtras(
    extras: Extras,
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalExtras provides extras,
    content = content
)

@Composable
fun rememberExtras(block: @NilDsl SettingsScope.() -> Unit): Extras {

    val extras = LocalExtras.current
    val density = LocalDensity.current

    val keyHash = currentCompositeKeyHash

    val scope = remember(extras) {
        SettingsScope(
            extras = extras.newBuilder().apply {
                set(DensityExtrasKey, density)
                set(CompositeKeyHash, keyHash)
            },
            interceptors = extras[InterceptorsExtras],
            fetchers = extras[FetchersExtra],
            decoders = extras[DecodersExtra]
        )
    }

    return remember(scope) { scope.apply(block).build() }
}
