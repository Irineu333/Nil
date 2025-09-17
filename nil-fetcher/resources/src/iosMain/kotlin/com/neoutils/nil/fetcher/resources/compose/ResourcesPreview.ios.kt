package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideExtras
import com.neoutils.nil.core.util.Extras

@Composable
actual fun ResourcesPreview(
    extras: Extras,
    content: @Composable (() -> Unit)
) {
    ProvideExtras(
        extras = extras,
        content = content,
    )
}
