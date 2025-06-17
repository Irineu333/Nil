package image.fetcher.network.extension

import image.core.util.Input
import image.fetcher.network.model.InputRequest

fun Input.Companion.request(
    url: String,
    config: InputRequest.Builder.() -> Unit = {}
) = InputRequest.Builder().apply(config).build(url)
