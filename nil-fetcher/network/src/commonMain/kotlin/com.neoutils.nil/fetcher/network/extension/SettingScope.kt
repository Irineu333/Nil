package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher

fun SettingScope<Fetcher<Input>>.network() {
    add(NetworkFetcher())
}