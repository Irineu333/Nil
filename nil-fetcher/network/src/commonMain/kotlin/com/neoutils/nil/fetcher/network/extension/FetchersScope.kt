package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.core.scope.FetchersScope
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher

fun FetchersScope.network() {
    add(NetworkFetcher())
}
