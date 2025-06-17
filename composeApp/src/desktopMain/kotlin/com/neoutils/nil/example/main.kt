package com.neoutils.nil.example

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.neoutils.nil.core.scope.ProvideSettings
import com.neoutils.nil.core.scope.rememberSettings
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ImageLoader-POC",
    ) {
        ProvideSettings(
            settings = rememberSettings {
                decoders(BitmapDecoder())
                fetchers(NetworkFetcher(), ResourcesFetcher())
            }
        ) {
            App()
        }
    }
}