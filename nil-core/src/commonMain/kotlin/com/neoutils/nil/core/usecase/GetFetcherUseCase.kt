package com.neoutils.nil.core.usecase

import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Resource

private val error = FetcherErrorStrings()

class GetFetcherUseCase {
    operator fun invoke(
        fetchers: List<Fetcher<*>>,
        request: Request
    ): Resource.Result<Fetcher<*>> {

        if (fetchers.isEmpty()) {
            return Resource.Result.Failure(NoFetcherFound(error.notFound))
        }

        val fetcher = fetchers.find { it.type == request::class }

        return if (fetcher != null) {
            Resource.Result.Success(fetcher)
        } else {
            Resource.Result.Failure(NoFetcherFound(error.noRequiredFound))
        }
    }
}