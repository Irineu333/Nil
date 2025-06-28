package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

private val error = FetcherErrorStrings()

val EnableProgressExtrasKey = Extras.Key(default = true)

class FetchInterceptor : Interceptor(Level.DATA) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        if (!chain.painter.isLoading) return flowOf(chain)
        if (!chain.data.isLoading) return flowOf(chain)

        val enableProgress = settings.extras[EnableProgressExtrasKey]

        return when (val fetcher = settings.fetcherFor(chain.request)) {
            is Resource.Result.Failure -> {
                flowOf(
                    chain.copy(
                        data = Resource.Result.Failure(fetcher.throwable)
                    )
                )
            }

            is Resource.Result.Success<Fetcher<Request>> if enableProgress -> {
                fetcher.value.fetch(
                    input = chain.request,
                    extras = settings.extras
                ).map { data ->
                    chain.copy(data = data)
                }
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                flowOf(
                    chain.copy(
                        data = fetcher.value.get(
                            input = chain.request,
                            extras = settings.extras
                        )
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
