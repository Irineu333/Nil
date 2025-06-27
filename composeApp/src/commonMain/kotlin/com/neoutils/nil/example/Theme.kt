package com.neoutils.nil.example

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideSettings
import com.neoutils.nil.core.composable.rememberSettings
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder
import com.neoutils.nil.fetcher.network.extension.network
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

            extras {
                network {
                    headers = mapOf(
                        "Authorization" to "Token"
                    )
                }
            }
        },
        content = content
    )
}