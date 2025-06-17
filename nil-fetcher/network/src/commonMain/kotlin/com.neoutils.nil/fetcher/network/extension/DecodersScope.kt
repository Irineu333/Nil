package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.fetcher.network.impl.NetworkFetcher
import com.neoutils.nil.core.scope.FetchersScope

fun FetchersScope.network() {
    fetchers.add(NetworkFetcher())
}