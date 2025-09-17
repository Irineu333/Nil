package com.neoutils.nil.fetcher.resources.extension

import com.neoutils.nil.core.extension.FetchersScope
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

fun FetchersScope.resources() {
    add(ResourcesFetcher())
}
