package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.usecase.GetFetcherUseCase
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.map

val ProgressMonitorExtrasKey = Extras.Key(default = true)

class FetchInterceptor(
    private val getFetcher: GetFetcherUseCase = GetFetcherUseCase()
) : Interceptor(Level.DATA) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {
        if (chain.painter != null) return ChainResult.Skip
        if (chain.data != null) return ChainResult.Skip

        val progressing = settings.extras[ProgressMonitorExtrasKey]

        return when (val fetcher = getFetcher(settings.fetchers, chain.request)) {
            is Resource.Result.Failure -> {
                ChainResult.Process(
                    chain.copy(
                        data = Resource.Result.Failure(fetcher.throwable)
                    )
                )
            }

            is Resource.Result.Success<Fetcher<Request>> if progressing -> {
                ChainResult.Process(
                    fetcher.value.fetch(
                        input = chain.request,
                        extras = settings.extras
                    ).map { data ->
                        chain.copy(data = data)
                    }
                )
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                ChainResult.Process(
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
}
