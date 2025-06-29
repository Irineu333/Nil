package com.neoutils.nil.example

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideSettings
import com.neoutils.nil.core.composable.rememberSettings
import com.neoutils.nil.fetcher.network.extension.network
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher
import com.neoutils.nil.interceptor.memoryCache.MemoryCacheInterceptor

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    ProvideSettings(
        settings = rememberSettings {

            fetchers = listOf(
                NetworkFetcher(),
                ResourcesFetcher()
            )

            interceptors += MemoryCacheInterceptor()

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