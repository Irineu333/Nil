package com.neoutils.nil.core.scope

import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.util.Input

data class FetchersScope(
    val fetchers: MutableList<Fetcher<Input>>
)