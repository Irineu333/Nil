package image.fetcher.network.extension

import image.core.scope.FetchersScope
import image.fetcher.network.impl.NetworkFetcher

fun FetchersScope.network() {
    fetchers.add(NetworkFetcher())
}