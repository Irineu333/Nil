package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private val error = FetcherErrorStrings()

val EnableProgressExtrasKey = Extras.Key(true)

class FetchInterceptor : Interceptor(Level.FETCHER) {
    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> = flow {

        val enableProgress = settings.extras[EnableProgressExtrasKey]

        when (val resource = settings.fetcherFor(chain.request)) {
            is Resource.Result.Failure -> {
                emit(
                    chain.copy(
                        data = Resource.Result.Failure(resource.throwable)
                    )
                )
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                if (enableProgress) {
                    emitAll(
                        resource.value.fetch(
                            input = chain.request,
                            extras = settings.extras
                        ).map { data ->
                            chain.copy(data = data)
                        }
                    )
                } else {
                    emit(
                        chain.copy(
                            data = resource.value.get(
                                input = chain.request,
                                extras = settings.extras
                            )
                        )
                    )
                }
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

enum class Level {
    REQUEST,
    FETCHER,
    DECODE,
}