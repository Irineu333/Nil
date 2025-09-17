package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.process
import com.neoutils.nil.core.extension.skip
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor
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

        if (chain.painter != null) return chain.skip()
        if (chain.data != null) return chain.skip()

        val progressing = settings.extras[ProgressMonitorExtrasKey]

        return when (val fetcher = getFetcher(settings.fetchers, chain.request)) {
            is Resource.Result.Failure -> {
                chain.process(
                    data = Resource.Result.Failure(fetcher.throwable)
                )
            }

            is Resource.Result.Success<Fetcher<Request>> if progressing -> {
                fetcher.value.fetch(
                    input = chain.request,
                    extras = settings.extras
                ).map { data ->
                    chain.copy(data = data)
                }.process()
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                chain.process(
                    data = fetcher.value.get(
                        input = chain.request,
                        extras = settings.extras
                    )
                )
            }
        }
    }
}
