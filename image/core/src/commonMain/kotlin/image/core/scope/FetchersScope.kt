package image.core.scope

import image.core.fetcher.Fetcher
import image.core.util.Input

data class FetchersScope(
    val fetchers: MutableList<Fetcher<Input>>
)