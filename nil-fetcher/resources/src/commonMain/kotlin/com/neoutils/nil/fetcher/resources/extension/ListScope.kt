package com.neoutils.nil.fetcher.resources.extension

import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

fun ListScope<Fetcher<Request>>.resources() {
    add(ResourcesFetcher())
}