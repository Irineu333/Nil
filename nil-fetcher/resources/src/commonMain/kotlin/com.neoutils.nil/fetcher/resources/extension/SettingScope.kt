package com.neoutils.nil.fetcher.resources.extension

import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

fun SettingScope<Fetcher<Input>>.resources() {
    add(ResourcesFetcher())
}