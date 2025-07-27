package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.composable.rememberSettings
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.DecodersScope
import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.core.scope.FetchersScope
import com.neoutils.nil.core.scope.InterceptorsScope
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.fetcher.resources.extension.resources

@Composable
fun ResourcesPreview(
    fetchers: @NilDsl FetchersScope.() -> Unit = { resources() },
    decoders: @NilDsl DecodersScope.() -> Unit = {},
    interceptors: @NilDsl InterceptorsScope.() -> Unit = {},
    extras: @NilDsl ExtrasScope.() -> Unit = {},
    content: @Composable () -> Unit
) = ResourcesPreview(
    settings = {
        fetchers(fetchers)
        decoders(decoders)
        interceptors(interceptors)
        interceptors(interceptors)
        extras(extras)
    },
    content = content,
)

@Composable
fun ResourcesPreview(
    settings: @NilDsl SettingsScope.() -> Unit,
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