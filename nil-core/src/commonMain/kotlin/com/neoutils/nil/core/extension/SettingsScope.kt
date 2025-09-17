package com.neoutils.nil.core.extension

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.interceptor.DecodersExtra
import com.neoutils.nil.core.interceptor.FetchersExtra
import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.scope.SettingsScope
import kotlin.collections.plus

typealias FetchersScope = ListScope<Fetcher<*>>
typealias DecodersScope = ListScope<Decoder>

fun SettingsScope.decoders(vararg decoders: Decoder) {
    extras.update(DecodersExtra) { it + decoders }
}

fun SettingsScope.fetchers(vararg fetchers: Fetcher<*>) {
    extras.update(FetchersExtra) { it + fetchers }
}

fun SettingsScope.decoders(scope: @NilDsl DecodersScope.() -> Unit) {
    extras.update(DecodersExtra) {
        ListScope
            .from(it)
            .apply(scope)
            .get()
    }
}

fun SettingsScope.fetchers(scope: @NilDsl FetchersScope.() -> Unit) {
    extras.update(FetchersExtra) {
        ListScope
            .from(it)
            .apply(scope)
            .get()
    }
}
