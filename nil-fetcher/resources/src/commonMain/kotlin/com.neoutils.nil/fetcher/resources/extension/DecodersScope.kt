package com.neoutils.nil.fetcher.resources.extension

import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher
import com.neoutils.nil.core.scope.FetchersScope

fun FetchersScope.resources() {
    fetchers.add(ResourcesFetcher())
}