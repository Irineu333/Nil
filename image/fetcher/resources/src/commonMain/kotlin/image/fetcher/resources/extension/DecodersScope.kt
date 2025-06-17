package image.fetcher.resources.extension

import image.core.scope.FetchersScope
import image.fetcher.resources.impl.ResourcesFetcher

fun FetchersScope.resources() {
    fetchers.add(ResourcesFetcher())
}