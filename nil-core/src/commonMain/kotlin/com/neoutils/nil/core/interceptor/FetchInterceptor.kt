package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource

private val error = FetcherErrorStrings()

class FetchInterceptor : Interceptor {
    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Chain {
        return when (val resource = settings.fetcherFor(chain.request)) {
            is Resource.Result.Failure -> {
                chain.copy(
                    data = Resource.Result.Failure(resource.throwable)
                )
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                chain.copy(
                    data = resource.value.get(
                        input = chain.request,
                        extras = settings.extras
                    )
                )
            }
        }
    }

    private fun Settings.fetcherFor(request: Request): Resource.Result<Fetcher<Request>> {

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