package image.fetcher.resources.extension

import image.core.fetcher.FetchersScope
import image.fetcher.resources.impl.ResourcesFetcher

fun FetchersScope.resources() {
    fetchers.add(ResourcesFetcher())
}