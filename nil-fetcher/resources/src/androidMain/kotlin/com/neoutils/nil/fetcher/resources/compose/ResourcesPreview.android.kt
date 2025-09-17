package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideExtras
import com.neoutils.nil.core.util.Extras
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@Composable
actual fun ResourcesPreview(
    extras: Extras,
    content: @Composable (() -> Unit)
) {
    // required to compose resources
    PreviewContextConfigurationEffect()

    ProvideExtras(
        extras = extras,
        content = content,
    )
}
