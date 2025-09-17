package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.composable.rememberSettings
import com.neoutils.nil.core.extension.fetchers
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.*
import com.neoutils.nil.fetcher.resources.extension.resources

@Composable
fun ResourcesPreview(
    settings: @NilDsl SettingsScope.() -> Unit = {
        fetchers {
            resources()
        }
    },
    content: @Composable () -> Unit
) = ResourcesPreview(
    settings = rememberSettings(settings),
    content = content,
)

@Composable
expect fun ResourcesPreview(
    settings: Settings,
    content: @Composable () -> Unit
)
