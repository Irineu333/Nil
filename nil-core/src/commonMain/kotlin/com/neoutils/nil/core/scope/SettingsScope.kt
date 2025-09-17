package com.neoutils.nil.core.scope

import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.DecodersExtra
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.FetchersExtra
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.foundation.InterceptorsExtras
import com.neoutils.nil.core.util.Extras
import kotlin.collections.plus

typealias FetchersScope = ListScope<Fetcher<*>>
typealias DecodersScope = ListScope<Decoder>
typealias InterceptorsScope = ListScope<Interceptor>

class SettingsScope internal constructor(
    var interceptors: List<Interceptor>,
    var fetchers: List<Fetcher<*>>,
    var decoders: List<Decoder>,
    val extras: Extras.Builder
) {
    fun interceptors(vararg interceptors: Interceptor) {
        this.interceptors += interceptors
    }

    fun decoders(vararg decoders: Decoder) {
        this.decoders += decoders
    }

    fun fetchers(vararg fetchers: Fetcher<*>) {
        this.fetchers += fetchers
    }

    fun interceptors(scope: @NilDsl InterceptorsScope.() -> Unit) {
        interceptors += InterceptorsScope().apply(scope).values
    }

    fun decoders(scope: @NilDsl DecodersScope.() -> Unit) {
        decoders += DecodersScope().apply(scope).values
    }

    fun fetchers(scope: @NilDsl FetchersScope.() -> Unit) {
        fetchers += FetchersScope().apply(scope).values
    }

    internal fun build() = extras.apply {
        set(InterceptorsExtras, interceptors)
        set(DecodersExtra, decoders)
        set(FetchersExtra, fetchers)
    }.build()
}
