package com.neoutils.nil.fetcher.network.extension

import com.neoutils.nil.fetcher.network.model.InputRequest
import com.neoutils.nil.core.util.Input

fun Input.Companion.request(
    url: String,
    config: InputRequest.Builder.() -> Unit = {}
) = InputRequest.Builder().apply(config).build(url)
