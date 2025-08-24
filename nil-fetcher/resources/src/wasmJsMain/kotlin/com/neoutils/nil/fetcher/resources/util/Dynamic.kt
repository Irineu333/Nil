@file:OptIn(ExperimentalStdlibApi::class)

package com.neoutils.nil.fetcher.resources.util

import com.neoutils.nil.core.util.fetchers
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

@EagerInitialization
private val initHook = run {
    fetchers.add(ResourcesFetcher())
}