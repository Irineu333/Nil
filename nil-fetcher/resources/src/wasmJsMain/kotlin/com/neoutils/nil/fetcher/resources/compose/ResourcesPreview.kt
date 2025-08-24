package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.model.Settings

@Composable
actual fun ResourcesPreview(
    settings: Settings,
    content: @Composable () -> Unit
) {
    // For web, we can simply render the content as resource loading works directly
    content()
}