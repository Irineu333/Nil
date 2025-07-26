@file:OptIn(ExperimentalStdlibApi::class)

package com.neoutils.nil.fetcher.network.util

import com.neoutils.nil.core.util.fetchers
import com.neoutils.nil.fetcher.network.impl.NetworkFetcher

@EagerInitialization
private val initHook = {
    fetchers.add(NetworkFetcher())
}.invoke()