package com.neoutils.nil.core.extension

import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource

private val error = FetcherErrorStrings()

internal fun List<Fetcher<Input>>.fetcherFor(input: Input): Resource.Result<Fetcher<Input>> {

    if (isEmpty()) {
        return Resource.Result.Failure(NoFetcherFound(error.notFound))
    }

    val fetcher = find { it.type == input::class }

    return if (fetcher != null) {
        Resource.Result.Success(fetcher)
    } else {
        Resource.Result.Failure(NoFetcherFound(error.noRequiredFound))
    }
}
