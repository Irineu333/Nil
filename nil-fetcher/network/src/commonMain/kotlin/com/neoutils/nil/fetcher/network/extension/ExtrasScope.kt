package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.fetcher.network.impl.HeadersExtrasKey
import com.neoutils.nil.fetcher.network.scope.NetworkScope

fun SettingsScope.network(
    scope: NetworkScope.() -> Unit
) {
    extras.update(HeadersExtrasKey) { headers ->
        NetworkScope(headers).apply(scope).headers
    }
}