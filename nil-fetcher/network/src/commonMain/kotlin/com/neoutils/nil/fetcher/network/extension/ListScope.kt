package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher

fun ListScope<Fetcher<Request>>.network() {
    add(NetworkFetcher())
}
