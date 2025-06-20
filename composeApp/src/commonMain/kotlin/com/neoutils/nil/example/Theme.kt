package com.neoutils.nil.example

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.scope.ProvideSettings
import com.neoutils.nil.core.scope.rememberSettings
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    ProvideSettings(
        settings = rememberSettings {
            decoders = listOf(
                BitmapDecoder()
            )

            fetchers = listOf(
                NetworkFetcher(),
                ResourcesFetcher()
            )
        },
        content = content
    )
}