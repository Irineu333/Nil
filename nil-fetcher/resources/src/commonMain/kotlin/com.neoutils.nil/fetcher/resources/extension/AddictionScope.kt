package com.neoutils.nil.fetcher.resources.extension

import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

fun AddictionScope<Fetcher<Input>>.resources() {
    add(ResourcesFetcher())
}