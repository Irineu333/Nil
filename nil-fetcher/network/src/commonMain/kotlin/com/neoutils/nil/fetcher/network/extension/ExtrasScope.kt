package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.core.scope.ExtrasScope
import com.neoutils.nil.fetcher.network.impl.HeadersExtrasKey
import com.neoutils.nil.fetcher.network.scope.NetworkScope

fun ExtrasScope.network(scope: NetworkScope.() -> Unit) {
    extras.update(HeadersExtrasKey) { headers ->
        NetworkScope(headers).apply(scope).headers
    }
}