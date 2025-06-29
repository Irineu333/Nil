package com.neoutils.nil.example

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideSettings
import com.neoutils.nil.core.composable.rememberSettings
import com.neoutils.nil.fetcher.network.extension.network

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    ProvideSettings(
        settings = rememberSettings {
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