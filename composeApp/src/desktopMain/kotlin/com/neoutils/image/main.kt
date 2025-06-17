package com.neoutils.image

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import image.core.scope.ProvideSettings
import image.core.scope.rememberSettings
import image.decoder.bitmap.impl.BitmapDecoder
import image.decoder.gif.impl.GifDecoder
import image.fetcher.network.impl.NetworkFetcher
import image.fetcher.resources.impl.ResourcesFetcher

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