package image.fetcher.network.extension

import image.core.fetcher.FetchersScope
import image.fetcher.network.impl.NetworkFetcher

fun FetchersScope.network() {
    fetchers.add(NetworkFetcher())
}